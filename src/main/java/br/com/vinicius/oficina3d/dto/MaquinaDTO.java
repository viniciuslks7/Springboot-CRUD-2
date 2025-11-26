package br.com.vinicius.oficina3d.dto;

public class MaquinaDTO {
    private Long id;
    private String modelo;
    private String status;
    private Long oficinaId;
    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getOficinaId() { return oficinaId; }
    public void setOficinaId(Long oficinaId) { this.oficinaId = oficinaId; }
}