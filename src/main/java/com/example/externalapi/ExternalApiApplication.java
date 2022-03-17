package com.example.externalapi;

import com.example.externalapi.mapper.ShareMapper;
import com.example.externalapi.service.MainService;
import com.example.externalapi.service.PortfolioService;
import com.example.externalapi.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ExternalApiApplication implements CommandLineRunner {

    private final MainService mainService;
    private final PortfolioService portfolioService;
    private final PositionService positionService;
    private final ShareMapper shareMapper;

//    public ExternalapiApplication(MainService mainService, PortfolioService portfolioService) {
//        this.mainService = mainService;
//        this.portfolioService = portfolioService;
//    }

    public static void main(String[] args) {
        SpringApplication.run(ExternalApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0) {
            System.out.println(args[0]);
        } else {
            System.out.println("fig te");

//            ShareDTO shareDTO = ShareCreateDTO.builder()
//                    .code("codecodecode")
//                    .ticker("TEST")
//                    .build();

//            Share share = shareMapper.toEntity(shareDTO);
//            System.out.println(share);


//            PositionInDTO.builder()
//                    .share(ShareDTO.builder()
//                            .code("BBG000QXWHD1")
//                            .ticker("BIDU")
//                            .id(1L)
//                            .build())
//                    .portfolio(PortfolioDTO.builder()
//                            .name("Основной")
//                            .id(1L)
//                            .build())
//                    .amount(100)
//                    .build();
//            var position = PositionInDTO.builder()
//                    .portfolioId(1L)
//                    .shareId(1L)
//                    .amount(100)
//                    .build();

//            System.out.println(positionService.add(position));

//            portfolioService.add("Основной");
            mainService.addShare();
        }
//        exit(0); // завершаем программу
    }
}

//1,BIDU,BBG000QXWHD1
