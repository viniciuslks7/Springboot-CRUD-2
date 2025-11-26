package br.com.vinicius.oficina3d.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "maquina")
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String modelo;

    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "oficina_id")
    private Oficina oficina;

    public Maquina() {}

    public Maquina(String modelo, String status) {
        this.modelo = modelo;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Oficina getOficina() { return oficina; }
    public void setOficina(Oficina oficina) { this.oficina = oficina; }
}