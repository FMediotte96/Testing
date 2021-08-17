package com.junitapp.ejemplos.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Facundo", new BigDecimal("1000.12345"));
//        cuenta.setPersona("Facundo");
        String expected = "Facundo";
        String actual = cuenta.getPersona();
        assertEquals(expected, actual);
//        assertTrue(actual.equals("Facundo"));
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Facundo", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); //Lo mismo que la anterior condición
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }
}