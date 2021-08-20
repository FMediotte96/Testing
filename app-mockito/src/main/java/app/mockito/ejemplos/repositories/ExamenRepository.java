package app.mockito.ejemplos.repositories;

import app.mockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
}
