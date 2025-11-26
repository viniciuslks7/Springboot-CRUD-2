package br.com.vinicius.oficina3d.dto;

public class OficinaDTO {
    private Long id;
    private String nome;
    private String local;
    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
}