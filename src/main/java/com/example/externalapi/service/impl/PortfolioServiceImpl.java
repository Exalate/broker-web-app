package com.example.externalapi.service.impl;

import com.example.externalapi.DTO.PortfolioDTO;
import com.example.externalapi.entity.Portfolio;
import com.example.externalapi.exception.DataConflict;
import com.example.externalapi.exception.NotFoundException;
import com.example.externalapi.mapper.PortfolioMapper;
import com.example.externalapi.repository.PortfolioRepository;
import com.example.externalapi.service.MainService;
import com.example.externalapi.service.PortfolioService;
import com.example.externalapi.service.PositionService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private static final String PORTFOLIO_NOT_FOUND = "Portfolio ID=%d not found";
    private static final String PORTFOLIO_NAME_ALREADY_EXIST = "Portfolio name=%s already exist, existing id=%d";
    private static final String CONFLICT_CURRENT_AND_SERVICE_DATA = "Conflict data between current and external";

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;
    private final MainService mainService;
    private final PositionService positionService;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, PortfolioMapper portfolioMapper,
                                MainService mainService, PositionService positionService) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMapper = portfolioMapper;
        this.mainService = mainService;
        this.positionService = positionService;
    }

    @Override
    @Transactional
    public PortfolioDTO save(PortfolioDTO portfolioDTO) {
        var portfolioDB = portfolioRepository.findFirstByName(portfolioDTO.getName());
        if (portfolioDB.isPresent()) {
            throw new EntityExistsException(String.format(
                    PORTFOLIO_NAME_ALREADY_EXIST, portfolioDTO.getName(), portfolioDB.get().getId()));
        }
        return portfolioMapper.toDto(portfolioRepository.save(portfolioMapper.toEntity(portfolioDTO)));
    }

    @Override
    @Transactional
    public PortfolioDTO add(String name) {
        return save(PortfolioDTO.builder().name(name).build());
    }

    @Override
    public PortfolioDTO get(Long id) {
        return portfolioRepository.findById(id).map(portfolioMapper::toDto)
                .orElseThrow(() -> new NotFoundException(String.format(PORTFOLIO_NOT_FOUND, id)));
    }

    @Override
    public List<PortfolioDTO> get() {
        return portfolioRepository.findAll().stream()
                .map(portfolioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PortfolioDTO update(PortfolioDTO portfolioDTO) {
        //TODO как правильно апдейтить?
        Portfolio portfolioDB = portfolioRepository.findById(portfolioDTO.getId())
                .orElseThrow(() -> new NotFoundException(String.format(PORTFOLIO_NOT_FOUND, portfolioDTO.getId())));
        portfolioRepository.findFirstByName(portfolioDTO.getName()).ifPresent(portfolio -> {
            throw new EntityExistsException(String.format(
                    PORTFOLIO_NAME_ALREADY_EXIST, portfolio.getName(), portfolio.getId()));
        });
        portfolioDB.setName(portfolioDTO.getName());
        return portfolioMapper.toDto(portfolioDB);
    }

    @Override
    public void delete(Long id) {
        //TODO правльное удаление(использование Optional)?
        portfolioRepository.findById(id).ifPresentOrElse(portfolioRepository::delete,
                () -> {
                    throw new NotFoundException(String.format(PORTFOLIO_NOT_FOUND, id));
                });
    }

    @Override
    @Transactional
    public PortfolioDTO correlateBroker(Long externalId, String name) throws DataConflict {

        //поиск по ID
        Optional<Portfolio> portfolioByExternalId = portfolioRepository.findFirstByExternalId(externalId);
        Portfolio portfolio;
        if (portfolioByExternalId.isPresent()) { //нашел 1-1
            portfolio = portfolioByExternalId.get();
            //проверить имя
            if (!portfolio.getName().equals(name)) {
                //имя не совпадает, нужно переименовать, но перед эти проверить,
                // а нет ли где то еще такого же имени, которое мы хотим установить сюда
                Optional<Portfolio> portfolioByNameOpt = portfolioRepository.findFirstByName(name);
                if (portfolioByNameOpt.isPresent()) { //нашелся с таким же именем
                    Portfolio conflictPortfolio = portfolioByNameOpt.get();
                    if (conflictPortfolio.getExternalId() > 0) { //заполнен ID
                        //КОНФЛИКТ external ID (есть элемент с "нашим именем"
                        // и у него заполнен другой(т.к. поиск по нашему ничего не нашел) ID)
                        //нужно брать этот ID, проверять его актуальность на "той" стороне
                        String namePortfolioById = mainService.getNamePortfolioById(
                                conflictPortfolio.getExternalId().toString());
                        if (name.equals(namePortfolioById)) {
                            throw new DataConflict(String.format(CONFLICT_CURRENT_AND_SERVICE_DATA
                                            + ": Incoming name and externalId have conflict internal portfolio ID=%d",
                                    conflictPortfolio.getId()));
                        } else {
                            //меняем наименование у другого, по его externalID оказывается другое имя
                            conflictPortfolio.setName(namePortfolioById);
                            portfolio.setName(name);
                        }
                    } else { //не заполнен ID
                        //Нужно установить ему наш ID
                        //Нельзя, т.к. есть запись с таким ID(нашлась в самом начале)
                        //нужно мержить(удалить перед этим переправив все ссылки на нашу изначальную portfolio)!!!!!!!!!!!!!!
                        // или просто удалять, если на нее нет ссылок например в Position
                        mergePortfolios(portfolio, conflictPortfolio);
                        portfolio.setName(name);
                    }
                } else {//не нашлось элементов с таким имененм
                    //переименовать и ок
                    portfolio.setName(name);
                }
            }
            portfolio.setIsBroker(true);
            return portfolioMapper.toDto(portfolio);
        } else { // не нашел 1-2

            //поиск по имени
            Optional<Portfolio> portfolioByName = portfolioRepository.findFirstByName(name);
            if (portfolioByName.isPresent()) {//нашел по имени
                //смотрим на externalID
                portfolio = portfolioByName.get();
                if (portfolio.getExternalId() > 0) {//---external ID заполнен
                    //------КОНФЛИКТ нашелся элемент с нашим именем, но другим external ID
                    //------нужно брать этот ID, проверять его актуальность на "той" стороне
                    String namePortfolioById = mainService.getNamePortfolioById(
                            portfolio.getExternalId().toString());

                    //Если имя на самом деле другое, меняем его у найденного и смело создаем новый
                    if (!portfolio.getName().equals(namePortfolioById)) {
                        portfolio.setName(namePortfolioById);
                        add(name, externalId);
                    }
                    throw new DataConflict(String.format(CONFLICT_CURRENT_AND_SERVICE_DATA
                                    + ": Incoming name and externalId have conflict internal portfolio ID=%d",
                            portfolio.getId()));
                } else {//---external ID не заполнен
                    //------подменяем external ID и
                    portfolio.setExternalId(externalId);
                }
            } else { //не нашел по имени
                //создаем новый элемент и
                return add(name, externalId);
            }
        }
        return portfolioMapper.toDto(portfolio);
    }

    //нужно мержить(удалить перед этим переправив все ссылки на нашу изначальную portfolio)!
    // или просто удалять, если на нее нет ссылок например в Position
    private void mergePortfolios(Portfolio portfolioKeep, Portfolio portfolioDelete) {
        positionService.replaceRelations(portfolioKeep, portfolioDelete);
        portfolioRepository.delete(portfolioDelete);
    }

    private PortfolioDTO add(String name, Long externalId) {
        return save(PortfolioDTO.builder()
                .name(name)
                .externalID(externalId)
                .isBroker(true)
                .build());
    }
}