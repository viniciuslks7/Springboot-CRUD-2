package br.com.vinicius.oficina3d.repository;

import br.com.vinicius.oficina3d.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MaquinaRepository extends JpaRepository<Maquina, Long> {
    java.util.List<Maquina> findByOficinaId(Long oficinaId);
    java.util.List<Maquina> findByUserId(Long userId);
}