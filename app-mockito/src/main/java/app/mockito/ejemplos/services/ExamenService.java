package app.mockito.ejemplos.services;

import app.mockito.ejemplos.models.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenByName(String nombre);
    Examen findExamenByNameConPreguntas(String nombre);
    Examen save(Examen examen);
}
