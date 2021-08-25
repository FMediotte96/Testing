package test.springboot.app.repositories;

import org.springframework.stereotype.Repository;
import test.springboot.app.models.Banco;

import java.util.List;

@Repository
public interface BancoRepository {
    List<Banco> findAll();

    Banco findById(Long id);

    void update(Banco banco);
}
