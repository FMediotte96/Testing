package app.mockito.ejemplos.services;

import app.mockito.ejemplos.models.Examen;
import app.mockito.ejemplos.repositories.ExamenRepository;
import app.mockito.ejemplos.repositories.ExamenRepositoryImpl;
import app.mockito.ejemplos.repositories.PreguntaRepository;
import app.mockito.ejemplos.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

//Habilitamos el uso de annotations para esta clase
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {

    @Spy
    private ExamenRepositoryImpl examenRepository;
    @Spy
    private PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    private ExamenServiceImpl service;

    @Test
    void testSpy() {
        // Spy requiere que se cree desde una clase concreta porque va a
        // llamar a la funcionalidad de la clase real no de un mock
        List<String> preguntas = Collections.singletonList("aritmética");
//        when(preguntaRepository2.findPreguntasByExamenId(anyLong())).thenReturn(preguntas);

        //Para evitar el comportamiento de que se está llamando al método
        doReturn(preguntas).when(preguntaRepository).findPreguntasByExamenId(anyLong());

        Examen examen = service.findExamenByNameConPreguntas("Matemáticas");
        assertEquals(5, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }
}