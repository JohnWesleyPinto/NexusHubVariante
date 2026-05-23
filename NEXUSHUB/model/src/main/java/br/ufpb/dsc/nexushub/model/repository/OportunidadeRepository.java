package br.ufpb.dsc.nexushub.model.repository;

import br.ufpb.dsc.nexushub.model.entity.Oportunidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
}
