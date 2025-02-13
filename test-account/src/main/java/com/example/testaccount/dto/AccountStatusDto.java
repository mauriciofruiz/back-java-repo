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
public class AccountStatusDto {

    @JsonProperty("date")
    private LocalDateTime date;

    @JsonProperty("clientName")
    private String clientName;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("initialBalance")
    private BigDecimal initialBalance;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("movement")
    private BigDecimal movement;

    @JsonProperty("finalBalance")
    private BigDecimal finalBalance;
}
