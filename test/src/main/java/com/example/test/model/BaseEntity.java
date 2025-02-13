package com.example.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntity {
    @CreatedDate
    @Column("fecha_creacion")
    @JsonProperty("registrationDate")
    private LocalDateTime registrationDate;

    @LastModifiedDate
    @Column("fecha_modificacion")
    @JsonProperty("modificationDate")
    private LocalDateTime modificationDate;
}
