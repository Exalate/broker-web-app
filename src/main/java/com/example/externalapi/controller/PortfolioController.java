package com.example.externalapi.controller;

import com.example.externalapi.DTO.PortfolioDTO;
import com.example.externalapi.service.PortfolioService;
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
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping()
    public List<PortfolioDTO> getAll() {
        return portfolioService.get();
    }

    @GetMapping("/{id}")
    public PortfolioDTO get(@PathVariable Long id) {
        return portfolioService.get(id);
    }

    @PostMapping()
    public PortfolioDTO create(@RequestBody PortfolioDTO portfolioDTO) {
        return portfolioService.save(portfolioDTO);
    }

    @PutMapping()
    public PortfolioDTO update(@RequestBody PortfolioDTO portfolioDTO) {
        return portfolioService.update(portfolioDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        portfolioService.delete(id);
    }
}
