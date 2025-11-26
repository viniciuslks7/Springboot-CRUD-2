package br.com.vinicius.oficina3d.repository;

import br.com.vinicius.oficina3d.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaquinaRepository extends JpaRepository<Maquina, Long> {
    List<Maquina> findByOficinaId(Long oficinaId);
}