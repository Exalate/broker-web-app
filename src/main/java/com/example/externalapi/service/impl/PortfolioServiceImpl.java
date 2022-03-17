package com.example.externalapi.service.impl;

import com.example.externalapi.DTO.PortfolioDTO;
import com.example.externalapi.entity.Portfolio;
import com.example.externalapi.exception.NotFoundException;
import com.example.externalapi.mapper.PortfolioMapper;
import com.example.externalapi.repository.PortfolioRepository;
import com.example.externalapi.service.PortfolioService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private static final String PORTFOLIO_NOT_FOUND = "Portfolio ID=%d not found";
    private static final String PORTFOLIO_NAME_ALREADY_EXIST = "Portfolio name=%s already exist, existing id=%d";

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, PortfolioMapper portfolioMapper) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMapper = portfolioMapper;
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
                () -> {throw new NotFoundException(String.format(PORTFOLIO_NOT_FOUND, id));
        });
    }
}