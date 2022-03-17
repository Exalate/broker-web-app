package com.example.externalapi.DTO.position;

import com.example.externalapi.DTO.PortfolioDTO;
import com.example.externalapi.DTO.share.ShareDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PositionDTO {
    private Long id;
    private PortfolioDTO portfolio;
    private ShareDTO share;
    private Integer amount;
}
