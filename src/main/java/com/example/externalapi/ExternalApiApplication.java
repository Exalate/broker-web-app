package com.example.externalapi;

import com.example.externalapi.mapper.ShareMapper;
import com.example.externalapi.repository.ShareRepository;
import com.example.externalapi.service.MainService;
import com.example.externalapi.service.PortfolioService;
import com.example.externalapi.service.PositionService;
import com.example.externalapi.service.ShareService;
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
    private final ShareRepository shareRepository;
    private final ShareService shareService;

    public static void main(String[] args) {
        SpringApplication.run(ExternalApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0) {
            System.out.println(args[0]);
        } else {
        }
    }
}
