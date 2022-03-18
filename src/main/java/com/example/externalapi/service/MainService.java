package com.example.externalapi.service;

import java.util.concurrent.ExecutionException;

public interface MainService {

    void addShare();

    void updatePortfolio() throws ExecutionException, InterruptedException;

    String getNamePortfolioById(String id);
}
