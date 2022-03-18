package com.example.externalapi.service;

import com.example.externalapi.DTO.position.PositionDTO;
import com.example.externalapi.DTO.position.PositionInDTO;
import com.example.externalapi.entity.Portfolio;

import java.util.List;

public interface PositionService {

    List<PositionDTO> getAll();

    PositionDTO get(Long id);

    PositionDTO create(PositionInDTO positionInDTO);

    PositionDTO update(PositionInDTO positionInDTO);

    void delete(Long id);

    List<PositionDTO> getAllPortfolioId(Long id);

    void replaceRelations(Portfolio portfolioKeep, Portfolio portfolioDelete);
}
