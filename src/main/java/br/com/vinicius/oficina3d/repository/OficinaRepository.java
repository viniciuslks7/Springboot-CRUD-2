package br.com.vinicius.oficina3d.repository;

import br.com.vinicius.oficina3d.model.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Long> {
}