package com.example.externalapi.controller;

import com.example.externalapi.DTO.PortfolioDTO;
import com.example.externalapi.service.PortfolioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import liquibase.pro.packaged.A;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/portfolios")
@RequiredArgsConstructor
@Api("Работа с портфелем")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @ApiOperation("Получить все портфели")
    @GetMapping()
    public List<PortfolioDTO> getAll() {
        return portfolioService.get();
    }

    @ApiOperation("Получить портфель по ID")
    @GetMapping("/{id}")
    public PortfolioDTO get(@PathVariable Long id) {
        return portfolioService.get(id);
    }

    @ApiOperation("Создать портфель")
    @PostMapping()
    public PortfolioDTO create(@RequestBody PortfolioDTO portfolioDTO) {
        return portfolioService.save(portfolioDTO);
    }

    @ApiOperation("Изменить портфель")
    @PutMapping()
    public PortfolioDTO update(@RequestBody PortfolioDTO portfolioDTO) {
        return portfolioService.update(portfolioDTO);
    }

    @ApiOperation("Удалить портфель")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        portfolioService.delete(id);
    }
}
