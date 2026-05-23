package br.ufpb.dsc.nexushub.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjetoRequest(
        @NotBlank String titulo,
        @NotBlank String descricao,
        String categoria,
        String responsavel
) {
}
