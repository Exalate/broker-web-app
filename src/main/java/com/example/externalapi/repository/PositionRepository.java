package com.example.externalapi.repository;

import com.example.externalapi.entity.Portfolio;
import com.example.externalapi.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findAllByPortfolio(Portfolio portfolio);

    @Query("SELECT p FROM Position p WHERE p.portfolio.id=?1")
    List<Position> findAllByPortfolioId(Long portfolioId);

    @Query("SELECT p FROM Position p WHERE p.portfolio.id=?1 and p.share.id=?2")
    Optional<Position> findByPortfolioIdAndShareId(Long portfolioId, Long shareId);
}
