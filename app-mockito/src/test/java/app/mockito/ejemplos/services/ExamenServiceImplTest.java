package app.mockito.ejemplos.services;

import app.mockito.ejemplos.models.Examen;
import app.mockito.ejemplos.repositories.ExamenRepository;
import app.mockito.ejemplos.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

//Habilitamos el uso de annotations para esta clase
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    private ExamenRepository examenRepository;
    @Mock
    private PreguntaRepository preguntaRepository;

    @InjectMocks
    private ExamenServiceImpl service;

    @BeforeEach
    void setUp() {
        //Habilitamos el uso de annotations para esta clase
        /*MockitoAnnotations.openMocks(this);

        //Simula una implementación de ExamenRepository
        examenRepository = mock(ExamenRepository.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(examenRepository, preguntaRepository);*/
    }

    @Test
    void findExamenByName() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
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

    @Test
    void testPreguntasExamen() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenByNameConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenByNameConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenByNameConPreguntas("Matemáticas");
        assertNull(examen);

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(5L);
    }
}