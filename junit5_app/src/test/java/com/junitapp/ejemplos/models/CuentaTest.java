package com.junitapp.ejemplos.models;

import com.junitapp.ejemplos.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

//Esto no se recomienda hacer porque se quiere buscar utilizar test unitarios con bajo acoplamiento
//entre ellos y no que dependan unos de otros, sin embargo se puede hacer.
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    private Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Facundo", new BigDecimal("1000.12345"));
        System.out.println("iniciando el método");
    }

    @AfterEach
    void tearDown() {
        System.out.println("finalizando el método de prueba.");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    @Test
    @DisplayName("probando el nombre de la cuenta corriente!")
    void testNombreCuenta() {
//        cuenta.setPersona("Facundo");
        String expected = "Facundo";
        String actual = cuenta.getPersona();
        assertNotNull(actual, "La cuenta no puede ser nula");
        assertEquals(expected, actual, () -> "El nombre de la cuenta no es el esperado: se esperaba " + expected
            + " sin embargo fue " + actual);
//        assertTrue(actual.equals("Facundo"), () -> "Nombre cuenta esperada debe ser igual a la real");
    }

    @Test
    @DisplayName("probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valor esperado.")
    void testSaldoCuenta() {
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); //Lo mismo que la anterior condición
    }

    @Test
    @DisplayName("testeando referencias que sean iguales con el método equals.")
    void testReferenciaCuenta() {
        cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }

    @Test
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Exception exception = assertThrows(DineroInsuficienteException.class,
            () -> cuenta.debito(new BigDecimal(1500))
        );

        String actual = exception.getMessage();
        String expected = "Dinero Insuficiente";
        assertEquals(expected, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Facundo", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    //@Disabled
    @DisplayName("probando relaciones entre las cuentas y el banco con assertAll")
    void testRelacionBancoCuentas() {
        //fail();
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Facundo", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertAll(() -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                "el valor del saldo de la cuenta2 no es el esperado"),
            () -> assertEquals("3000", cuenta1.getSaldo().toPlainString(),
                "el valor del saldo de la cuenta1 no es el esperado"),
            () -> assertEquals(2, banco.getCuentas().size(), "el banco no tiene las cuentas esperadas"),
            () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre()),
            () -> assertEquals("Facundo",
                banco.getCuentas().stream()
                    .filter(c -> c.getPersona().equals("Facundo"))
                    .findFirst()
                    .get()
                    .getPersona()),
            () -> assertTrue(banco.getCuentas().stream()
                .anyMatch(c -> c.getPersona().equals("John Doe")))
        );
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows() {
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void testSoloLinuxMac() {
    }

    @Test
    @DisabledOnOs(OS.LINUX)
    void testNoLinux() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_9)
    void soloJdk9() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void soloJdk8() {
    }

    @Test
    @DisabledOnJre(JRE.JAVA_8)
    void testNoJdk8() {
    }

    @Test
    void imprimirSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k,v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = ".*8.*")
    void testJavaVersion() {
    }

    @Test
    @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
    void testSolo64() {
    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
    void testNo64() {
    }

    @Test
    @EnabledIfSystemProperty(named = "user.name", matches = "facundo.mediotte")
    void testUsername() {
    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void testDev() {
    }
}