package br.ufpb.dsc.nexushub.model.service.impl;

import br.ufpb.dsc.nexushub.model.dto.ProjetoRequest;
import br.ufpb.dsc.nexushub.model.dto.ProjetoResponse;
import br.ufpb.dsc.nexushub.model.entity.Projeto;
import br.ufpb.dsc.nexushub.model.repository.ProjetoRepository;
import br.ufpb.dsc.nexushub.model.service.ProjetoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjetoServiceImpl implements ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoServiceImpl(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjetoResponse> listar() {
        return projetoRepository.findAll()
                .stream()
                .map(ProjetoResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public ProjetoResponse criar(ProjetoRequest request) {
        Projeto projeto = new Projeto(
                request.titulo(),
                request.descricao(),
                request.categoria(),
                request.responsavel()
        );

        return ProjetoResponse.from(projetoRepository.save(projeto));
    }
}
