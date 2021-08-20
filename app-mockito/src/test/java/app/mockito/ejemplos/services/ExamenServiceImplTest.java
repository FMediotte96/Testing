package app.mockito.ejemplos.services;

import app.mockito.ejemplos.models.Examen;
import app.mockito.ejemplos.repositories.ExamenRepository;
import app.mockito.ejemplos.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExamenServiceImplTest {

    private ExamenRepository examenRepository;
    private ExamenService service;
    private PreguntaRepository preguntaRepository;

    @BeforeEach
    void setUp() {
        //Simula una implementación de ExamenRepository
        examenRepository = mock(ExamenRepository.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(examenRepository, preguntaRepository);
    }

    @Test
    void findExamenByName() {
        List<Examen> datos = Arrays.asList(
            new Examen(5L, "Matemáticas"),
            new Examen(6L, "Lenguaje"),
            new Examen(7L, "Historia")
        );

        when(examenRepository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenByName("Matemáticas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow(null).getId());
        assertEquals("Matemáticas", examen.get().getNombre());
    }

    @Test
    void findExamenByNameListaVacia() {
        List<Examen> datos = Collections.emptyList();

        when(examenRepository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenByName("Matemáticas");

        assertFalse(examen.isPresent());
    }
}