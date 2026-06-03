package br.ufpb.dsc.nexushub.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
public class Oportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    private String tipo;
    private String contato;
    private LocalDate prazo;

    protected Oportunidade() {
    }

    public Oportunidade(String titulo, String descricao, String tipo, String contato, LocalDate prazo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.contato = contato;
        this.prazo = prazo;
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

    public String getTipo() {
        return tipo;
    }

    public String getContato() {
        return contato;
    }

    public LocalDate getPrazo() {
        return prazo;
    }
}
