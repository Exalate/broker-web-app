package com.example.externalapi.controller;

import com.example.externalapi.DTO.PortfolioDTO;
import com.example.externalapi.entity.Portfolio;
import com.example.externalapi.repository.PortfolioRepository;
import com.example.externalapi.repository.PositionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PortfolioControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ObjectMapper objectMapper;


    private Portfolio portfolio;

    @Before
    public void setUp() throws Exception {
        positionRepository.deleteAllInBatch();
        portfolioRepository.deleteAllInBatch();

        portfolio = portfolioRepository.save(Portfolio.builder()
                .name("тест портфель1")
                .isBroker(false)
                .build());
    }

    @After
    public void tearDown() {
        portfolioRepository.deleteAllInBatch();
        positionRepository.deleteAllInBatch();
    }

    @Test
    public void getPortfolios() throws Exception {
        mockMvc.perform(get("/portfolios")).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.equalTo(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is(portfolio.getName())));
    }

    @Test
    public void getPortfolio() throws Exception {
        mockMvc.perform(get("/portfolios/" + portfolio.getId())).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.is(portfolio.getName())));
    }

    @Test
    public void createPortfolio() throws Exception {
        PortfolioDTO portfolioDTO = PortfolioDTO.builder().isBroker(false).name("тест создания портфеля").build();
        String req = objectMapper.writeValueAsString(portfolioDTO);
        mockMvc.perform(post("/portfolios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.notNullValue()))
                .andExpect(jsonPath("name", Matchers.equalTo(portfolioDTO.getName())));
    }

    @Test
    public void updatePortfolio() throws Exception {
        PortfolioDTO portfolioDTO = PortfolioDTO.builder().isBroker(false).name("замена имени портфеля").build();
        portfolioDTO.setId(portfolio.getId());
        String req = objectMapper.writeValueAsString(portfolioDTO);
        mockMvc.perform(put("/portfolios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.equalTo(portfolio.getId().intValue())))
                .andExpect(jsonPath("name", Matchers.equalTo(portfolioDTO.getName())));
    }

    @Test
    public void deletePortfolio() throws Exception {
        mockMvc.perform(delete("/portfolios/" + portfolio.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        assertTrue(portfolioRepository.findAll().isEmpty());
    }
}
