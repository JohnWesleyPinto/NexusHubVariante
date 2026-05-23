package br.ufpb.dsc.nexushub.model.repository;

import br.ufpb.dsc.nexushub.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
