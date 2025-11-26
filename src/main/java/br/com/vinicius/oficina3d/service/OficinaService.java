package br.com.vinicius.oficina3d.service;

import br.com.vinicius.oficina3d.model.Oficina;
import br.com.vinicius.oficina3d.repository.OficinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OficinaService {

    private final OficinaRepository repo;

    public OficinaService(OficinaRepository repo) {
        this.repo = repo;
    }

    public List<Oficina> findAll() { return repo.findAll(); }
    public Optional<Oficina> findById(Long id) { return repo.findById(id); }
    public Oficina save(Oficina o) { return repo.save(o); }
    public void deleteById(Long id) { repo.deleteById(id); }
}