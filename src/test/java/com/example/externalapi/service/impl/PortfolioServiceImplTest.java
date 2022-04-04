package com.example.externalapi.service.impl;

import com.example.externalapi.DTO.PortfolioDTO;
import com.example.externalapi.entity.Portfolio;
import com.example.externalapi.repository.PortfolioRepository;
import com.example.externalapi.repository.PositionRepository;
import com.example.externalapi.service.PortfolioService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PortfolioServiceImplTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Before
    public void setUp() {
        positionRepository.deleteAllInBatch();
        portfolioRepository.deleteAllInBatch();
    }

    @After
    public void tearDown() {
        positionRepository.deleteAllInBatch();
        portfolioRepository.deleteAllInBatch();
    }

    @Test
    public void correlateBrokerExternalIdExistAndItNameEqual() {
        //Нашел существующий ExternalID и его name совпал
        assertTrue(portfolioRepository.findAll().isEmpty());

        Portfolio portfolio = portfolioRepository.save(Portfolio.builder()
                .isBroker(true)
                .externalId(999L)
                .name("999")
                .build());

        assertEquals(1, portfolioRepository.findAll().size());

        portfolioService.correlateBroker(portfolio.getExternalId(), portfolio.getName());

        //должен у существующего изменить isBroker, если требуется
        assertEquals(1, portfolioRepository.findAll().size());
    }

    @Test
    public void correlateBrokerExternalIdExistAndItNameNotEqualsAndNotExistInAnotherPortfolio() {
        //Нашел существующий ExternalID и его name НЕ совпал, при этом такого name нет в других Portfolio
        assertTrue(portfolioRepository.findAll().isEmpty());

        final Portfolio portfolio = portfolioRepository.save(Portfolio.builder()
                .isBroker(true)
                .externalId(999L)
                .name("999")
                .build());

        final String notEqualsName = "555";

        assertEquals(1, portfolioRepository.findAll().size());

        final PortfolioDTO portfolioDTO = portfolioService.correlateBroker(portfolio.getExternalId(), notEqualsName);

        //должен у существующего изменить isBroker, если требуется
        assertEquals(1, portfolioRepository.findAll().size());
        assertEquals(notEqualsName, portfolioDTO.getName());
        assertTrue(portfolioDTO.getIsBroker());
    }

    @Test
    public void correlateBrokerExternalIdExistAndItNameNotEqualsAndExistInAnotherPortfolioWithEmptyExternalId() {
        //Нашел существующий ExternalID и его name НЕ совпал, при этом такой name есть в другом Portfolio,
        //                                                                      и у этого Portfolio пустой ExternalID
        assertTrue(portfolioRepository.findAll().isEmpty());

        final String notEqualsName = "555";

        final Portfolio portfolio1 = portfolioRepository.save(Portfolio.builder()
                .isBroker(true)
                .externalId(999L)
                .name("999")
                .build());

        portfolioRepository.save(Portfolio.builder()
                .isBroker(false)
//                .externalId(null)
                .name(notEqualsName)
                .build());

        assertEquals(2, portfolioRepository.findAll().size());

        final PortfolioDTO portfolioDTO = portfolioService.correlateBroker(portfolio1.getExternalId(), notEqualsName);

        //должен у существующего изменить isBroker, если требуется
        //элементы нужно слить в один, поэтому в результате работы сервиса в этом случае должен остаться один элемент
        assertEquals(1, portfolioRepository.findAll().size());
        assertEquals(notEqualsName, portfolioDTO.getName());
        assertTrue(portfolioDTO.getIsBroker());
    }

}