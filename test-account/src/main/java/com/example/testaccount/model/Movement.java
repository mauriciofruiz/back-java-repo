package com.example.testaccount.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Table("test.movimientos")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Movement extends BaseEntity{
    @Id
    @Column("id_movimiento")
    @JsonProperty("movementId")
    private Integer movementId;

    @Column("fecha_movimiento")
    @JsonProperty("movementDate")
    private LocalDateTime movementDate;

    @Column("id_cuenta_movimiento")
    @JsonProperty("accountMovementId")
    private Integer accountMovementId;

    @Column("valor_movimiento")
    @JsonProperty("movementValue")
    private BigDecimal movementValue;

    @Column("saldo_movimiento")
    @JsonProperty("movementBalance")
    private BigDecimal movementBalance;
}
