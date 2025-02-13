package com.example.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Table("test.persona")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Person extends BaseEntity{
    @Id
    @Column("id_persona")
    @JsonProperty("personId")
    private Integer personId;

    @Column("nombre_persona")
    @JsonProperty("personName")
    private String personName;

    @Column("genero_persona")
    @JsonProperty("personGender")
    private String personGender;

    @Column("edad_persona")
    @JsonProperty("personAge")
    private Integer personAge;

    @Column("identificacion_persona")
    @JsonProperty("personIdentification")
    private String personIdentification;

    @Column("direccion_persona")
    @JsonProperty("personAddress")
    private String personAddress;

    @Column("telefono_persona")
    @JsonProperty("personPhone")
    private String personPhone;
}
