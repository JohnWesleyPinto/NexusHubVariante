package br.ufpb.dsc.nexushub.controller;

import br.ufpb.dsc.nexushub.model.dto.GrupoRequest;
import br.ufpb.dsc.nexushub.model.dto.GrupoResponse;
import br.ufpb.dsc.nexushub.model.groups.service.GroupService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/grupos")
public class GrupoRestController {

    private final GroupService groupService;

    public GrupoRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GrupoResponse> listar() {
        return groupService.listActiveGroups().stream().map(GrupoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoResponse> obterPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(GrupoResponse.from(groupService.getGroup(id)));
    }

    @PostMapping
    public GrupoResponse criar(@Valid @RequestBody GrupoRequest request) {
        return GrupoResponse.from(groupService.createGroup(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

}
