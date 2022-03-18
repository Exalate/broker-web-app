package com.example.externalapi.service;

import com.example.externalapi.DTO.PortfolioDTO;

import java.util.List;

public interface PortfolioService {
    PortfolioDTO save(PortfolioDTO portfolioDTO);

    PortfolioDTO add(String name);

    PortfolioDTO get(Long id);

    List<PortfolioDTO> get();

    PortfolioDTO update(PortfolioDTO portfolioDTO);

    void delete(Long id);

    PortfolioDTO correlateBroker(Long externalId, String name);
}
