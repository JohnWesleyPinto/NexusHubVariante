package br.ufpb.dsc.nexushub.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    private String area;
    private String responsavel;

    protected Grupo() {
    }

    public Grupo(String nome, String descricao, String area, String responsavel) {
        this.nome = nome;
        this.descricao = descricao;
        this.area = area;
        this.responsavel = responsavel;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getArea() {
        return area;
    }

    public String getResponsavel() {
        return responsavel;
    }
}
