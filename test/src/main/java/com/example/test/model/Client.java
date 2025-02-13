package com.example.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Table("test.cliente")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Client extends BaseEntity{
    @Id
    @Column("id_cliente")
    @JsonProperty("clientId")
    private Integer clientId;

    @Column("id_persona_cliente")
    @JsonProperty("personClientId")
    private Integer personClientId;

    @Column("password_cliente")
    @JsonProperty("clientPassword")
    private String clientPassword;

    @Column("estado_cliente")
    @JsonProperty("clientStatus")
    private Boolean clientStatus;
}
