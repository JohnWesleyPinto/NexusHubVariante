package br.ufpb.dsc.nexushub.controller;

import br.ufpb.dsc.nexushub.model.entity.Oportunidade;
import br.ufpb.dsc.nexushub.model.repository.OportunidadeRepository;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/oportunidades")
public class OportunidadeRestController {

    private final OportunidadeRepository oportunidadeRepository;

    public OportunidadeRestController(OportunidadeRepository oportunidadeRepository) {
        this.oportunidadeRepository = oportunidadeRepository;
    }

    @GetMapping
    public List<Oportunidade> listar() {
        return oportunidadeRepository.findAll();
    }
}
