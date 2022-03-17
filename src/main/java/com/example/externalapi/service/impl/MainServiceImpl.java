package com.example.externalapi.service.impl;

import com.example.externalapi.DTO.share.ShareCreateDTO;
import com.example.externalapi.service.MainService;
import com.example.externalapi.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.PositionsResponse;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.OperationsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final InvestApi investApi;
    private final ShareService shareService;

    private void ttt() {
        List<Account> accountsId = investApi.getUserService().getAccountsSync();
        accountsId.forEach(account -> {
            try {
                OperationsService operationsService = investApi.getOperationsService();
                PositionsResponse positionsSync = operationsService.getPositionsSync(account.getId());
                for (int a = 0; a < positionsSync.getSecuritiesCount(); a++) {
                    var figi = positionsSync.getSecurities(a).getFigi();
                    Instrument instrument = investApi.getInstrumentsService().getInstrumentByFigiSync(figi).get();
                    if (!"share".equals(instrument.getInstrumentType())) {
                        continue;
                    }
                    var share = ShareCreateDTO.builder()
                            .code(figi)
                            .ticker(instrument.getTicker())
                            .build();
                    var shareDB = shareService.create(share);
                    System.out.println(">>>в базе:");
                    System.out.println(shareDB);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    @Override
    public void addShare() {

        ttt();

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
}
