package app.mockito.ejemplos.repositories;

import java.util.List;

public interface PreguntaRepository {
    void saveSome(List<String> preguntas);
    List<String> findPreguntasByExamenId(Long id);
}
