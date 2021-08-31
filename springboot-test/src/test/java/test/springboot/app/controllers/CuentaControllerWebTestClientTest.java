package test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import test.springboot.app.models.Cuenta;
import test.springboot.app.models.TransactionDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CuentaControllerWebTestClientTest {

    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {
        // Given
        TransactionDto dto = new TransactionDto();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal("100"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito!");
        response.put("transaction", dto);

        // When
        client.post().uri("/api/cuentas/transferir")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .exchange()
            // Then
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .consumeWith(respuesta -> {
                try {
                    JsonNode json = objectMapper.readTree(respuesta.getResponseBody());
                    assertEquals("Transferencia realizada con éxito!", json.path("mensaje").asText());
                    assertEquals(1, json.path("transaction").path("cuentaOrigenId").asLong());
                    assertEquals(LocalDate.now().toString(), json.path("date").asText());
                    assertEquals("100", json.path("transaction").path("monto").asText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            })
            .jsonPath("$.mensaje").isNotEmpty()
            .jsonPath("$.mensaje").value(is("Transferencia realizada con éxito!"))
            .jsonPath("$.mensaje").value(valor -> assertEquals("Transferencia realizada con éxito!", valor))
            .jsonPath("$.mensaje").isEqualTo("Transferencia realizada con éxito!")
            .jsonPath("$.transaction.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
            .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
            .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(2)
    void testDetalle() throws JsonProcessingException {
        //Given
        Cuenta cuenta = new Cuenta(1L, "Facundo", new BigDecimal("900"));

        //When
        client.get().uri("/api/cuentas/1")
            .exchange()
            //Then
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.persona").isEqualTo("Facundo")
            .jsonPath("$.saldo").isEqualTo(900)
            .json(objectMapper.writeValueAsString(cuenta));
    }

    @Test
    @Order(3)
    void testDetalle2() {
        //When
        client.get().uri("/api/cuentas/2")
            .exchange()
            //Then
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Cuenta.class)
            .consumeWith(response -> {
                Cuenta cuenta = response.getResponseBody();
                assertNotNull(cuenta);
                assertEquals("John", cuenta.getPersona());
                assertEquals("2100.00", cuenta.getSaldo().toPlainString());
            });
    }

}