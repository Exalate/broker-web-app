package com.example.externalapi.repository;

import com.example.externalapi.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Optional<Share> findFirstByCode(String code);

    Optional<Share> findByTickerIsLike(String ticker);
}
