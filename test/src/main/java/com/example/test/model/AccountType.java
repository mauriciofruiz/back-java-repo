package com.example.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Table("tipo_cuenta")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountType extends BaseEntity{
    @Id
    @Column("id_tipo_cuenta")
    @JsonProperty("accountTypeId")
    private Integer accountTypeId;

    @Column("descripcion_tipo_cuenta")
    @JsonProperty("accountTypeDescription")
    private String accountTypeDescription;
}
