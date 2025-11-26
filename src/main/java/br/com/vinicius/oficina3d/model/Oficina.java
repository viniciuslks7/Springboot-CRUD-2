package br.com.vinicius.oficina3d.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oficina")
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    private String local;

    @OneToMany(mappedBy = "oficina", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Maquina> maquinas = new ArrayList<>();

    public Oficina() {}

    public Oficina(String nome, String local) {
        this.nome = nome;
        this.local = local;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public List<Maquina> getMaquinas() { return maquinas; }
    public void setMaquinas(List<Maquina> maquinas) { this.maquinas = maquinas; }

    public void addMaquina(Maquina m) {
        maquinas.add(m);
        m.setOficina(this);
    }

    public void removeMaquina(Maquina m) {
        maquinas.remove(m);
        m.setOficina(null);
    }
}