package com.example.externalapi.service.impl;

import com.example.externalapi.entity.Portfolio;
import com.example.externalapi.repository.PortfolioRepository;
import com.example.externalapi.service.MainService;
import com.example.externalapi.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final InvestApi investApi;
    private final PortfolioService portfolioService;
    private final PortfolioRepository portfolioRepository;



//    private void ttt() {
//        List<Account> accountsId = investApi.getUserService().getAccountsSync();
//        accountsId.forEach(account -> {
//            try {
//                OperationsService operationsService = investApi.getOperationsService();
//                PositionsResponse positionsSync = operationsService.getPositionsSync(account.getId());
//                for (int a = 0; a < positionsSync.getSecuritiesCount(); a++) {
//                    var figi = positionsSync.getSecurities(a).getFigi();
//                    Instrument instrument = investApi.getInstrumentsService().getInstrumentByFigiSync(figi).get();
//                    if (!"share".equals(instrument.getInstrumentType())) {
//                        continue;
//                    }
//                    var share = ShareCreateDTO.builder()
//                            .code(figi)
//                            .ticker(instrument.getTicker())
//                            .build();
//                    var shareDB = shareService.create(share);
//                    System.out.println(">>>в базе:");
//                    System.out.println(shareDB);
//                }
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        });
//    }

    @Override
    public void addShare() {

//        ttt();

//        try {
//            System.out.println(investApi.getInstrumentsService().getAllShares().get()
//                    .stream()
//                    .filter(share -> "usd".equals(share.getNominal().getCurrency()) &&
//                            "SPB".equals(share.getExchange()))
//                    .map(Share::getTicker)
//                    .sorted()
//                    .collect(Collectors.joining(", "))
//            );
//        }
//        catch (Exception e) {
//
//        }
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
