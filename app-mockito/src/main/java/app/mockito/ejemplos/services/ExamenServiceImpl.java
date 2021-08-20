package app.mockito.ejemplos.services;

import app.mockito.ejemplos.models.Examen;
import app.mockito.ejemplos.repositories.ExamenRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {
    private ExamenRepository examenRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public Optional<Examen> findExamenByName(String nombre) {
        return examenRepository.findAll()
            .stream()
            .filter(e -> e.getNombre().equals(nombre))
            .findFirst();
    }
}
