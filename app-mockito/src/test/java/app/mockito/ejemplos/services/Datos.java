package app.mockito.ejemplos.services;

import app.mockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public static final List<Examen> EXAMENES = Arrays.asList(
        new Examen(5L, "Matemáticas"),
        new Examen(6L, "Lenguaje"),
        new Examen(7L, "Historia")
    );

    public static final List<Examen> EXAMENES_ID_NULL = Arrays.asList(
        new Examen(null, "Matemáticas"),
        new Examen(null, "Lenguaje"),
        new Examen(null, "Historia")
    );

    public static final List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(
        new Examen(-5L, "Matemáticas"),
        new Examen(-6L, "Lenguaje"),
        new Examen(null, "Historia")
    );

    public static final List<String> PREGUNTAS = Arrays.asList(
        "aritmética", "integrales", "derivadas", "trigonometría", "geometría"
    );

    public static final Examen EXAMEN = new Examen(null, "Física");
}
