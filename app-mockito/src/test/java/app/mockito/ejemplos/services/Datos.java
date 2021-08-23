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

    public static final List<String> PREGUNTAS = Arrays.asList(
        "aritmética", "integrales", "derivadas", "trigonometría", "geometría"
    );

    public static final Examen EXAMEN = new Examen(8L, "Física");
}
