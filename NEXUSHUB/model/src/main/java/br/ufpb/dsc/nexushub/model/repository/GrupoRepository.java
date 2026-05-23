package br.ufpb.dsc.nexushub.model.repository;

import br.ufpb.dsc.nexushub.model.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
}
