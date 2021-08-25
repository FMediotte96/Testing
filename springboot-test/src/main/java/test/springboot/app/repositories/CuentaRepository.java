package test.springboot.app.repositories;

import org.springframework.stereotype.Repository;
import test.springboot.app.models.Cuenta;

import java.util.List;

@Repository
public interface CuentaRepository {
    List<Cuenta> findAll();

    Cuenta findById(Long id);

    void update(Cuenta cuenta);
}
