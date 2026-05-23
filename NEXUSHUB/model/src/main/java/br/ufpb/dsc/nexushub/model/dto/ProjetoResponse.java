package br.ufpb.dsc.nexushub.model.dto;

import br.ufpb.dsc.nexushub.model.entity.Projeto;
import java.time.LocalDateTime;

public record ProjetoResponse(
        Long id,
        String titulo,
        String descricao,
        String categoria,
        String responsavel,
        String status,
        Integer pontos,
        LocalDateTime criadoEm
) {
    public static ProjetoResponse from(Projeto projeto) {
        return new ProjetoResponse(
                projeto.getId(),
                projeto.getTitulo(),
                projeto.getDescricao(),
                projeto.getCategoria(),
                projeto.getResponsavel(),
                projeto.getStatus(),
                projeto.getPontos(),
                projeto.getCriadoEm()
        );
    }
}
