package com.example.externalapi.DTO.position;

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
public class PositionInDTO {
    private Long id;
    private Long portfolioId;
    private Long shareId;
    private Integer amount;
}