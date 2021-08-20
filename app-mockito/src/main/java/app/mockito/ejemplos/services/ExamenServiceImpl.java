package app.mockito.ejemplos.services;

import app.mockito.ejemplos.models.Examen;
import app.mockito.ejemplos.repositories.ExamenRepository;
import app.mockito.ejemplos.repositories.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {
    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Optional<Examen> findExamenByName(String nombre) {
        return examenRepository.findAll()
            .stream()
            .filter(e -> e.getNombre().equals(nombre))
            .findFirst();
    }

    @Override
    public Examen findExamenByNameConPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenByName(nombre);
        Examen examen = null;
        if (examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow(null);
            List<String> preguntas = preguntaRepository.findPreguntasByExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }
}
