package app.mockito.ejemplos.repositories;

import app.mockito.ejemplos.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryOtro implements ExamenRepository {

    @Override
    public Examen save(Examen examen) {
        return null;
    }

    @Override
    public List<Examen> findAll() {
        try {
            System.out.println("ExamenRepositoryOtro");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
