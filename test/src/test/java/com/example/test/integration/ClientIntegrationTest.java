package com.example.test.integration;

import com.example.test.dto.PersonClientDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String SUCESS_MESSAGE = "Transacción realizada con éxito.";

    @Test
    public void testCreateClient() {
        PersonClientDto clientDto = new PersonClientDto(null, null, "Mauricio Ruiz","Masculino", 28, "ID0001", "Cumbaya", "0999123456", "password123", true);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/clientes", clientDto, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(SUCESS_MESSAGE, response.getBody());
    }

    @Test
    public void testGetClient() {
        ResponseEntity<PersonClientDto> response = restTemplate.getForEntity("/api/clientes/1", PersonClientDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getClientId());
    }
}
