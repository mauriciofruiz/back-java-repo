package com.example.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PersonClientDtoResponse {

    @JsonProperty("clientId")
    private Integer clientId;

    @JsonProperty("personClientId")
    private Integer personClientId;

    @JsonProperty("personName")
    private String personName;

    @JsonProperty("personAddress")
    private String personAddress;

    @JsonProperty("personPhone")
    private String personPhone;

    @JsonProperty("clientPassword")
    private String clientPassword;

    @JsonProperty("clientStatus")
    private Boolean clientStatus;

}
