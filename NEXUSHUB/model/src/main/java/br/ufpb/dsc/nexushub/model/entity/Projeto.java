package br.ufpb.dsc.nexushub.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    private String categoria;
    private String responsavel;
    private String status;
    private Integer pontos;
    private LocalDateTime criadoEm;

    protected Projeto() {
    }

    public Projeto(String titulo, String descricao, String categoria, String responsavel) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.responsavel = responsavel;
    }

    @PrePersist
    void prePersist() {
        this.status = this.status == null ? "ABERTO" : this.status;
        this.pontos = this.pontos == null ? 0 : this.pontos;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getStatus() {
        return status;
    }

    public Integer getPontos() {
        return pontos;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
