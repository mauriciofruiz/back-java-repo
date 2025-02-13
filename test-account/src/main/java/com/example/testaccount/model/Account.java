package com.example.testaccount.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Table("test.cuenta")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Account extends BaseEntity{
    @Id
    @Column("id_cuenta")
    @JsonProperty("accountId")
    private Integer accountId;

    @Column("id_cliente_cuenta")
    @JsonProperty("accountClientId")
    private Integer accountClientId;

    @Column("numero_cuenta")
    @JsonProperty("accountNumber")
    private String accountNumber;

    @Column("id_tipo_cuenta")
    @JsonProperty("accountTypeId")
    private Integer accountTypeId;

    @Column("saldo_inicial_cuenta")
    @JsonProperty("initialBalance")
    private BigDecimal initialBalance;

    @Column("estado_cuenta")
    @JsonProperty("accountStatus")
    private Boolean accountStatus;

}
