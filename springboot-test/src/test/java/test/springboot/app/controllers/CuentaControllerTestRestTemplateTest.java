package test.springboot.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import test.springboot.app.models.TransactionDto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CuentaControllerTestRestTemplateTest {

    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate client;

    @LocalServerPort
    private String port;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() {
        TransactionDto dto = new TransactionDto();
        dto.setMonto(new BigDecimal("100"));
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setBancoId(1L);

        ResponseEntity<String> response =
            client.postForEntity(crearUri("/api/cuentas/transferir"), dto, String.class);
        System.out.println(port);

        String json = response.getBody();
        System.out.println(json);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transferencia realizada con éxito!"));
        assertTrue(json.contains("{\"cuentaOrigenId\":1,\"cuentaDestinoId\":2,\"monto\":100,\"bancoId\":1},\"status\":\"OK\"}"));
    }

    private String crearUri(String uri) {
        return "http://localhost:" + port + uri;
    }
}