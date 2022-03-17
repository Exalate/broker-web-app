package com.example.externalapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
@PropertySource("classpath:token.properties")
public class TinkoffApi {

    @Value("${tinkoff.token}")
    private String token;

    @Bean
    public InvestApi investApi() {
        return InvestApi.create(token);
    }
}
