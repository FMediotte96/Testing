package app.mockito.ejemplos.repositories;

import app.mockito.ejemplos.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntaRepositoryImpl implements PreguntaRepository {

    @Override
    public void saveSome(List<String> preguntas) {
        System.out.println("PreguntaRepositoryImpl.saveSome");
    }

    @Override
    public List<String> findPreguntasByExamenId(Long id) {
        System.out.println("PreguntaRepositoryImpl.findPreguntasByExamenId");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Datos.PREGUNTAS;
    }
}
