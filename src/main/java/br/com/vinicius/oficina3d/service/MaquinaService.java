package br.com.vinicius.oficina3d.service;

import br.com.vinicius.oficina3d.model.Maquina;
import br.com.vinicius.oficina3d.repository.MaquinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaquinaService {

    private final MaquinaRepository repo;

    public MaquinaService(MaquinaRepository repo) {
        this.repo = repo;
    }

    public List<Maquina> findAll() { return repo.findAll(); }
    public Optional<Maquina> findById(Long id) { return repo.findById(id); }
    public List<Maquina> findByOficinaId(Long oficinaId) { return repo.findByOficinaId(oficinaId); }
    public List<Maquina> findByUserId(Long userId) { return repo.findByUserId(userId); }
    public Maquina save(Maquina m) { return repo.save(m); }
    public void deleteById(Long id) { repo.deleteById(id); }
}