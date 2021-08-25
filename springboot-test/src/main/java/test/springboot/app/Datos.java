package test.springboot.app;

import test.springboot.app.models.Banco;
import test.springboot.app.models.Cuenta;

import java.math.BigDecimal;

public class Datos {

    private Datos() {
    }

    public static final Cuenta CUENTA_001 = new Cuenta(1L, "Facundo", new BigDecimal("1000"));
    public static final Cuenta CUENTA_002 = new Cuenta(2L, "John", new BigDecimal("2000"));
    public static final Banco BANCO = new Banco(1L, "El banco financiero", 0);
}
