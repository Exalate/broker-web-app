package com.example.externalapi.service.impl;

import com.example.externalapi.DTO.position.PositionDTO;
import com.example.externalapi.DTO.position.PositionInDTO;
import com.example.externalapi.entity.Portfolio;
import com.example.externalapi.entity.Position;
import com.example.externalapi.exception.IncorrectOperation;
import com.example.externalapi.exception.NotFoundException;
import com.example.externalapi.mapper.PositionMapper;
import com.example.externalapi.repository.PositionRepository;
import com.example.externalapi.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private static final String POSITION_NOT_FOUND = "Position not found ID=%d";
    private static final String POSITION_ALREADY_EXIST_COMBINATION_PORTFOLIO_SHARE =
            "Position already exist(ID=%d) with current combination of portfolio and share";
    private static final String ERROR_DELETE = "Position cannot be deleted ID=%d, because amount>0";

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    @Override
    public List<PositionDTO> getAll() {
        return positionRepository.findAll().stream()
                .map(positionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PositionDTO get(Long id) {
        return positionMapper.toDto(positionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(POSITION_NOT_FOUND, id))));
    }

    @Override
    @Transactional
    public PositionDTO create(PositionInDTO positionInDTO) {
        //TODO правильно использовать "чужой" сервис, или нужно использовать "чужой" репозиторий, нужно конверить опять в ентити
        //не должно быть позиций с одним Share и Portfolio
        positionRepository.findByPortfolioIdAndShareId(positionInDTO.getPortfolioId(), positionInDTO.getShareId())
                .ifPresent(position -> {
                    throw new EntityExistsException(
                            String.format(POSITION_ALREADY_EXIST_COMBINATION_PORTFOLIO_SHARE, position.getId()));
                });
        return positionMapper.toDto(positionRepository.save(positionMapper.toEntity(positionInDTO)));
    }

    @Override
    @Transactional
    public PositionDTO update(PositionInDTO positionInDTO) {
        Position positionDB = positionRepository.findById(positionInDTO.getId())
                .orElseThrow(() -> new NotFoundException(String.format(POSITION_NOT_FOUND, positionInDTO.getId())));
        positionDB.setAmount(positionInDTO.getAmount());
        return positionMapper.toDto(positionDB);
    }

    @Override
    public void delete(Long id) {
        positionRepository.findById(id).ifPresentOrElse(
                position -> {
                    if (position.getAmount() > 0) {
                        throw new IncorrectOperation(String.format(ERROR_DELETE, id));
                    }
                    positionRepository.delete(position);
                },
                () -> {
                    throw new NotFoundException(String.format(POSITION_NOT_FOUND, id));
                }
        );
    }

    @Override
    public List<PositionDTO> getAllPortfolioId(Long id) {
        return positionRepository.findAllByPortfolioId(id).stream()
                .map(positionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void replaceRelations(Portfolio portfolioKeep, Portfolio portfolioDelete) {
        positionRepository.findAllByPortfolio(portfolioDelete)
            .forEach(position -> position.setPortfolio(portfolioKeep));
    }

}
