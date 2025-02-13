package com.example.test.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para la clase Client
 */
public class ClientTest {

    @Test
    public void testClientCreation() {
        Client client = new Client(1, 2, "password123", true);

        assertNotNull(client);
        assertEquals(1, client.getClientId());
        assertEquals(2, client.getPersonClientId());
        assertEquals("password123", client.getClientPassword());
        assertTrue(client.getClientStatus());
    }

    @Test
    public void testClientSetters() {
        Client client = new Client();
        client.setClientId(1);
        client.setPersonClientId(2);
        client.setClientPassword("password123");
        client.setClientStatus(true);

        assertEquals(1, client.getClientId());
        assertEquals(2, client.getPersonClientId());
        assertEquals("password123", client.getClientPassword());
        assertTrue(client.getClientStatus());
    }
}
