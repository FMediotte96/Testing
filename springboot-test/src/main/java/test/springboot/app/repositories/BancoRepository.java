package test.springboot.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import test.springboot.app.models.Banco;

public interface BancoRepository extends JpaRepository<Banco, Long> {
}
