package app.mockito.ejemplos;

import app.mockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    private Datos() {
    }

    private static final String MATEMATICAS = "Matemáticas";
    private static final String LENGUAJE = "Lenguaje";
    private static final String HISTORIA = "Historia";

    public static final List<Examen> EXAMENES = Arrays.asList(
        new Examen(5L, MATEMATICAS),
        new Examen(6L, LENGUAJE),
        new Examen(7L, HISTORIA)
    );

    public static final List<Examen> EXAMENES_ID_NULL = Arrays.asList(
        new Examen(null, MATEMATICAS),
        new Examen(null, LENGUAJE),
        new Examen(null, HISTORIA)
    );

    public static final List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(
        new Examen(-5L, MATEMATICAS),
        new Examen(-6L, LENGUAJE),
        new Examen(null, HISTORIA)
    );

    public static final List<String> PREGUNTAS = Arrays.asList(
        "aritmética", "integrales", "derivadas", "trigonometría", "geometría"
    );

    public static final Examen EXAMEN = new Examen(null, "Física");
}
