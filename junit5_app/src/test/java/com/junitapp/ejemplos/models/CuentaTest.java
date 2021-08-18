package com.junitapp.ejemplos.models;

import com.junitapp.ejemplos.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

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

    @Nested
    @DisplayName("probando atributos de la cuenta corriente")
    class CuentaTestNombreSaldo {
        @Test
        @DisplayName("el nombre")
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
        @DisplayName("el saldo, que no sea null, mayor que cero, valor esperado.")
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
    }

    @Nested
    class CuentaOperacionesTest {
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
        void testTransferirDineroCuentas() {
            Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Facundo", new BigDecimal("1500.8989"));

            Banco banco = new Banco();
            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta1.getSaldo().toPlainString());
        }
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

    @Nested
    class SistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
            assertTrue(true);
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
            assertTrue(true);
        }

        @Test
        @DisabledOnOs(OS.LINUX)
        void testNoLinux() {
            assertTrue(true);
        }
    }

    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_9)
        void soloJdk9() {
            assertTrue(true);
        }

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJdk8() {
            assertTrue(true);
        }

        @Test
        @DisabledOnJre(JRE.JAVA_8)
        void testNoJdk8() {
            assertTrue(true);
        }
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> System.out.println(k + ":" + v));
            assertTrue(true);
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*8.*")
        void testJavaVersion() {
            assertTrue(true);
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64() {
            assertTrue(true);
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNo64() {
            assertTrue(true);
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "facundo.mediotte")
        void testUsername() {
            assertTrue(true);
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
            assertTrue(true);
        }
    }

    @Nested
    class VariableAmbienteTest {
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> env = System.getenv();
            env.forEach((k, v) -> System.out.println(k + " = " + v));
            assertTrue(true);
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*java-11-openjdk.*")
        void testJavaHome() {
            assertTrue(true);
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "12")
        void testProcesadores() {
            assertTrue(true);
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
            assertTrue(true);
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled() {
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("test saldo cuenta dev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(esDev); //Si es falso deshabilita la prueba de forma programatica
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); //Lo mismo que la anterior condición
    }

    @Test
    @DisplayName("test saldo cuenta dev 2")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(esDev, () -> { //Si es falso no ejecuta la expresión lambda
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.123456, cuenta.getSaldo().doubleValue());
        });
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); //Lo mismo que la anterior condición
    }

    @DisplayName("Probando Debito Cuenta Repetir!")
    @RepeatedTest(value = 5, name = "{displayName} - Repetición numero {currentRepetition} de {totalRepetitions}")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if (info.getCurrentRepetition() == 3) {
            System.out.println("estamos en la repetición " + info.getCurrentRepetition());
        }
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @ParameterizedTest(name = "número {index} ejecutando con valor {0} - {argumentsWithNames}")
    @ValueSource(strings = {"100", "200", "300", "500", "700", "1000.12345"})
    void testDebitoCuentaValueSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "número {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000.12345"})
    void testDebitoCuentaCsvSource(String index, String monto) {
        System.out.println(index + " -> " + monto);
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "número {index} ejecutando con valor {0} - {argumentsWithNames}")
    @CsvFileSource(resources = "/data.csv")
    void testDebitoCuentaCsvFileSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "número {index} ejecutando con valor {0} - {argumentsWithNames}")
    @MethodSource("montoList")
    void testDebitoCuentaMethodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "700", "1000.12345");
    }
}