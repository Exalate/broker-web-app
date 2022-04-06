package com.example.externalapi.service.impl;

import com.example.externalapi.DTO.PortfolioDTO;
import com.example.externalapi.entity.Portfolio;
import com.example.externalapi.exception.DataConflict;
import com.example.externalapi.repository.PortfolioRepository;
import com.example.externalapi.repository.PositionRepository;
import com.example.externalapi.service.MainService;
import com.example.externalapi.service.PortfolioService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PortfolioServiceImplTest {

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private PositionRepository positionRepository;

    @MockBean
    private MainService mainService;

    @Autowired
    private PortfolioService portfolioService;

    private Portfolio portfolio1;

    @Before
    public void setUp() {
        positionRepository.deleteAllInBatch();
        portfolioRepository.deleteAllInBatch();

        portfolio1 = portfolioRepository.save(Portfolio.builder()
                .isBroker(true)
                .externalId(999L)
                .name("999")
                .build());
    }

    @After
    public void tearDown() {
        positionRepository.deleteAllInBatch();
        portfolioRepository.deleteAllInBatch();
    }

    //Должен быть уникален name
    //Должен быть уникален externalId(если заполнен)
    //Если externalId заполнен, isBroker должен быть в значении true

    @Test
    public void correlateBrokerExternalIdExistAndItNameEqual() {
        //Нашел существующий ExternalID и его name совпал
        portfolioService.correlateBroker(portfolio1.getExternalId(), portfolio1.getName());

        //должен у существующего изменить isBroker, если требуется
        assertEquals(1, portfolioRepository.findAll().size());
    }

    @Test
    public void correlateBrokerExternalIdExistAndItNameNotEqualsAndNotExistInAnotherPortfolio() {
        //Нашел существующий ExternalID и его name НЕ совпал, при этом такого name нет в других Portfolio

        final String notEqualsName = "555";

        final PortfolioDTO portfolioDTO = portfolioService.correlateBroker(portfolio1.getExternalId(), notEqualsName);

        //должен у существующего изменить isBroker, если требуется
        assertEquals(1, portfolioRepository.findAll().size());
        assertEquals(notEqualsName, portfolioDTO.getName());
        assertTrue(portfolioDTO.getIsBroker());
    }

    @Test
    public void correlateBrokerExternalIdExistAndItNameNotEqualsAndExistInAnotherPortfolioWithEmptyExternalId() {
        //Нашел существующий ExternalID и его name НЕ совпал, при этом такой name есть в другом Portfolio,
        //                                                                      и у этого Portfolio пустой ExternalID
        final String notEqualsName = "555";

        portfolioRepository.save(Portfolio.builder()
                .isBroker(false)
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

    @Test
    public void correlateBrokerExternalIdExistAndItNameNotEqualsAndExistInAnotherPortfolioWithNotEmptyExternalId() {
        //Нашел существующий ExternalID и его name НЕ совпал, при этом такой name есть в другом Portfolio,
        //                                       и у этого Portfolio НЕ пустой ExternalID и он есть во внешнем сервисе

        final String notEqualsName = "555";

        portfolioRepository.save(Portfolio.builder()
                .isBroker(true)
                .externalId(888L)
                .name(notEqualsName)
                .build());

        given(mainService.getNamePortfolioById("888")).willReturn("555");

        //должен быть exception
        assertThrows(DataConflict.class, () ->
                portfolioService.correlateBroker(portfolio1.getExternalId(), notEqualsName));
    }

    @Test
    public void correlateBrokerExternalIdExistAndItNameNotEqualsAndExistInAnotherPortfolioWithNotEmptyExternalIdAndItNotExistExternal() {
        //Нашел существующий ExternalID и его name НЕ совпал, при этом такой name есть в другом Portfolio,
        //                                       и у этого Portfolio НЕ пустой ExternalID и в сервисе другое имя

        String notEqualsName = "555";

        portfolioRepository.save(Portfolio.builder()
                .isBroker(true)
                .externalId(888L)
                .name(notEqualsName)
                .build());

        given(mainService.getNamePortfolioById("888")).willReturn("name from external service");
        //конфликтующий портфель будет переименован

        final PortfolioDTO portfolioDTO = portfolioService.correlateBroker(portfolio1.getExternalId(), notEqualsName);
        Portfolio portfolio2Check = portfolioRepository.findFirstByExternalId(888L).orElseThrow();

        assertEquals(notEqualsName, portfolioDTO.getName());
        assertEquals("name from external service", portfolio2Check.getName());
    }

    @Test
    public void correlateBrokerExternalIdNotExist() {
        //по externalId не нашлось, по name не нашлось

        Long newExternalId = 351654L;
        String newName = "not found name";

        Portfolio portfolioBeforeCheck = portfolioRepository.findFirstByExternalId(newExternalId).orElse(null);

        portfolioService.correlateBroker(newExternalId, newName);

        Portfolio portfolioAfterCheck = portfolioRepository.findFirstByExternalId(newExternalId).orElseThrow();
        //будет создана новая запись

        assertNull(portfolioBeforeCheck);
        assertEquals(newExternalId, portfolioAfterCheck.getExternalId());
        assertEquals(newName, portfolioAfterCheck.getName());
    }

    @Test
    public void correlateBrokerExternalIdNotExistAndNameExist() {
        //по externalId не нашлось, по name нашел и там пустой externalId
        String newName = "new name";
        Long newExternalId = 351654L;

        portfolioRepository.save(Portfolio.builder()
                .isBroker(false)
                .name(newName)
                .build());

        portfolioService.correlateBroker(newExternalId, newName);

        Portfolio portfolioChekAfter = portfolioRepository.findFirstByName(newName).orElse(null);

        assertNotNull(portfolioChekAfter);
        assertEquals(true, portfolioChekAfter.getIsBroker());
        assertEquals(newExternalId, portfolioChekAfter.getExternalId());
    }

    @Test
    public void correlateBrokerExternalIdNotExistAndNameExistAndThereNotEmptyExternalId() {
        //по externalId не нашлось, по name нашел и там НЕ пустой externalId
        //                  externalId не совпадают, т.к. мы бы сюда не попали
        String name = "new name";
        Long newExternalId = 351654L;

        portfolioRepository.save(Portfolio.builder()
                .isBroker(true)
                .name(name)
                .externalId(newExternalId)
                .build());

        given(mainService.getNamePortfolioById(newExternalId.toString())).willReturn("name from external service");

        portfolioService.correlateBroker(444444L, name);

        Portfolio portfolioConflictAfter = portfolioRepository.findFirstByExternalId(newExternalId).orElse(null);
        Portfolio newPortfolio = portfolioRepository.findFirstByExternalId(444444L).orElse(null);

        assertNotNull(portfolioConflictAfter);
        assertEquals("name from external service", portfolioConflictAfter.getName());
        assertEquals(true, portfolioConflictAfter.getIsBroker());

        assertNotNull(newPortfolio);
        assertEquals(name, newPortfolio.getName());
        assertEquals(true, newPortfolio.getIsBroker());

    }

    @Test
    public void correlateBrokerExternalIdNotExistAndNameExistAndThereNotEmptyExternalIdAndExternalNameEquals() {
        //по externalId не нашлось, по name нашел и там НЕ пустой externalId
        //                  externalId не совпадают, т.к. мы бы сюда не попали

        given(mainService.getNamePortfolioById("999")).willReturn(portfolio1.getName());

        assertThrows(DataConflict.class, () -> portfolioService.correlateBroker(444L, portfolio1.getName()));
    }

}