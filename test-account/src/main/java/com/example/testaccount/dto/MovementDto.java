package com.example.testaccount.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MovementDto {

    @JsonProperty("accountMovementId")
    private Integer accountMovementId;

    @JsonProperty("movementValue")
    private BigDecimal movementValue;
}
