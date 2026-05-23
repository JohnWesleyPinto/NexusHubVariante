package br.ufpb.dsc.nexushub.controller;

import br.ufpb.dsc.nexushub.model.dto.ProjetoRequest;
import br.ufpb.dsc.nexushub.model.dto.ProjetoResponse;
import br.ufpb.dsc.nexushub.model.service.ProjetoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/projetos")
public class ProjetoRestController {

    private final ProjetoService projetoService;

    public ProjetoRestController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @GetMapping
    public List<ProjetoResponse> listar() {
        return projetoService.listar();
    }

    @PostMapping
    public ResponseEntity<ProjetoResponse> criar(@Valid @RequestBody ProjetoRequest request) {
        ProjetoResponse projeto = projetoService.criar(request);
        return ResponseEntity.created(URI.create("/api/projetos/" + projeto.id())).body(projeto);
    }
}
