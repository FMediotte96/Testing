package app.mockito.ejemplos.services;

import app.mockito.ejemplos.Datos;
import app.mockito.ejemplos.models.Examen;
import app.mockito.ejemplos.repositories.ExamenRepository;
import app.mockito.ejemplos.repositories.ExamenRepositoryImpl;
import app.mockito.ejemplos.repositories.PreguntaRepository;
import app.mockito.ejemplos.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
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
    private ExamenRepositoryImpl examenRepository;
    @Mock
    private PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    private ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
        //Habilitamos el uso de annotations para esta clase
        /*MockitoAnnotations.openMocks(this);

        //Simula una implementación de ExamenRepository
        examenRepository = mock(ExamenRepositoryImpl.class);
        preguntaRepository = mock(PreguntaRepositoryImpl.class);
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
        // Given
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        // when
        Examen examen = service.findExamenByNameConPreguntas("Matemáticas");

        // then
        assertNull(examen);
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(5L);
    }

    @Test
    void testSaveExamen() {
        // Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        when(examenRepository.save(any(Examen.class))).then(new Answer<Examen>() {
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        // When
        Examen examen = service.save(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(examenRepository).save(any(Examen.class));
        verify(preguntaRepository).saveSome(anyList());
    }

    @Test
    void testManejoException() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepository.findPreguntasByExamenId(isNull())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.findExamenByNameConPreguntas("Matemáticas"));
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(isNull());
    }

    @Test
    void testArgumentMatchers() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNameConPreguntas("Matemáticas");
        verify(examenRepository).findAll();
//        verify(preguntaRepository).findPreguntasByExamenId(argThat(arg -> arg != null && arg.equals(5L)));
        verify(preguntaRepository).findPreguntasByExamenId(argThat(arg -> arg != null && arg >= 5L));
//        verify(preguntaRepository).findPreguntasByExamenId(eq(5L));
    }

    @Test
    void testArgumentMatchers2() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNameConPreguntas("Matemáticas");
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(argThat(new MiArgsMatchers()));
    }

    @Test
    void testArgumentMatchers3() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNameConPreguntas("Matemáticas");
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(argThat(
            (argument) -> argument != null && argument > 0)
        );
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long> {
        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "es para un mensaje personalizado de error " +
                "que imprime mockito en caso de que falle el test " +
                argument + " debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentCaptor() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNameConPreguntas("Matemáticas");

//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntaRepository).findPreguntasByExamenId(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        //DoThrow se utiliza con los métodos void
        doThrow(IllegalArgumentException.class).when(preguntaRepository).saveSome(anyList());

        assertThrows(IllegalArgumentException.class, () -> service.save(examen));
    }

    @Test
    void testDoAnswer() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5L ? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasByExamenId(anyLong());
        //El when que va seguido dsp de un do encapsula el mock que se pasa como argumento

        Examen examen = service.findExamenByNameConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("geometría"));
        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());

        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testDoAnswerSaveExamen() {
        // Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        doAnswer(new Answer<Examen>() {
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(examenRepository).save(any(Examen.class));

        // When
        Examen examen = service.save(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(examenRepository).save(any(Examen.class));
        verify(preguntaRepository).saveSome(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        doCallRealMethod().when(preguntaRepository).findPreguntasByExamenId(anyLong());
        Examen examen = service.findExamenByNameConPreguntas("Matemáticas");
        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
    }

    @Test
    void testSpy() {
        // Spy requiere que se cree desde una clase concreta porque va a
        // llamar a la funcionalidad de la clase real no de un mock
        ExamenRepository examenRepository2 = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository2 = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository2, preguntaRepository2);

        List<String> preguntas = Collections.singletonList("aritmética");
//        when(preguntaRepository2.findPreguntasByExamenId(anyLong())).thenReturn(preguntas);

        //Para evitar el comportamiento de que se está llamando al método
        doReturn(preguntas).when(preguntaRepository2).findPreguntasByExamenId(anyLong());

        Examen examen = examenService.findExamenByNameConPreguntas("Matemáticas");
        assertEquals(5, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        verify(examenRepository2).findAll();
        verify(preguntaRepository2).findPreguntasByExamenId(anyLong());
    }
}