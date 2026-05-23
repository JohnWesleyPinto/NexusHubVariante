package br.ufpb.dsc.nexushub.model.service;

import br.ufpb.dsc.nexushub.model.dto.ProjetoRequest;
import br.ufpb.dsc.nexushub.model.dto.ProjetoResponse;
import java.util.List;

public interface ProjetoService {

    List<ProjetoResponse> listar();

    ProjetoResponse criar(ProjetoRequest request);
}
