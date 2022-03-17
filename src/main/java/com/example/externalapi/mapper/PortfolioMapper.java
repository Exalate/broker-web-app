package com.example.externalapi.mapper;

import com.example.externalapi.DTO.PortfolioDTO;
import com.example.externalapi.entity.Portfolio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PortfolioMapper {

    private final ModelMapper modelMapper;

    public PortfolioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Portfolio toEntity(PortfolioDTO portfolioDTO) {
        return Objects.isNull(portfolioDTO) ? null : modelMapper.map(portfolioDTO, Portfolio.class);
    }

    public PortfolioDTO toDto(Portfolio portfolio) {
        return Objects.isNull(portfolio) ? null : modelMapper.map(portfolio, PortfolioDTO.class);
    }

}
