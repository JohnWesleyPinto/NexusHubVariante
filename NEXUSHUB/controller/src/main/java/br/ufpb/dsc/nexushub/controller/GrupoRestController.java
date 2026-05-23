package br.ufpb.dsc.nexushub.controller;

import br.ufpb.dsc.nexushub.model.entity.Grupo;
import br.ufpb.dsc.nexushub.model.repository.GrupoRepository;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/grupos")
public class GrupoRestController {

    private final GrupoRepository grupoRepository;

    public GrupoRestController(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    @GetMapping
    public List<Grupo> listar() {
        return grupoRepository.findAll();
    }
}
