package com.example.externalapi.service.impl;

import com.example.externalapi.service.MainService;
import com.example.externalapi.service.PortfolioService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.core.InvestApi;

@Service
public class MainServiceImpl implements MainService {

    private final InvestApi investApi;
    private final PortfolioService portfolioService;

    public MainServiceImpl(InvestApi investApi,@Lazy PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
        this.investApi = investApi;
    }

    @Override
    public void addShare() {

    }

    @Override
    public void updatePortfolio() {

        //Получить портфели

        var accounts = investApi.getUserService().getAccountsSync();

        accounts.stream().forEach(account -> {
            portfolioService.correlateBroker(Long.parseLong(account.getId()), account.getName());
        });

        //Записать их с ID, если таких нет











        //портфели
//        var accounts = investApi.getUserService().getAccountsSync();

        System.out.println(accounts);

        //портфель
        Account account = accounts.stream().findFirst().orElseThrow();
        System.out.println(account.getName());

        var accountId = accounts.stream().findFirst().orElseThrow().getId();

        //Получаем и печатаем портфолио
        var portfolio = investApi.getOperationsService().getPortfolioSync(accountId);

//        System.out.println(portfolio);


//        try {
//            List<Share> shares = investApi.getInstrumentsService().getAllShares().get().stream()
//                    ;
//        }
//        catch (InterruptedException | ExecutionException exception) {
//            System.out.println(exception.getLocalizedMessage());
//        }
//
//
////                log.info("список ценно-бумажных позиций портфеля");
//        var securities = positions.getSecurities();
//        for (SecurityPosition security : securities) {
//            var figi = security.getFigi();
//            var balance = security.getBalance();
//            var blocked = security.getBlocked();
//            log.info("figi: {}, текущий баланс: {}, заблокировано: {}", figi, balance, blocked);
//        }
    }

    @Override
    public String getNamePortfolioById(String id) {
        var accounts = investApi.getUserService().getAccountsSync();
        return accounts.stream()
                .filter(account -> id.equals(account.getId()))
                .map(Account::getName)
                .findFirst()
                .orElse("");
    }
}
