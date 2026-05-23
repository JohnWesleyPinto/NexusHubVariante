package br.ufpb.dsc.nexushub.controller.config;

import br.ufpb.dsc.nexushub.model.entity.Grupo;
import br.ufpb.dsc.nexushub.model.entity.Oportunidade;
import br.ufpb.dsc.nexushub.model.entity.Projeto;
import br.ufpb.dsc.nexushub.model.repository.GrupoRepository;
import br.ufpb.dsc.nexushub.model.repository.OportunidadeRepository;
import br.ufpb.dsc.nexushub.model.repository.ProjetoRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProjetoRepository projetoRepository;
    private final GrupoRepository grupoRepository;
    private final OportunidadeRepository oportunidadeRepository;

    public DataSeeder(
            ProjetoRepository projetoRepository,
            GrupoRepository grupoRepository,
            OportunidadeRepository oportunidadeRepository
    ) {
        this.projetoRepository = projetoRepository;
        this.grupoRepository = grupoRepository;
        this.oportunidadeRepository = oportunidadeRepository;
    }

    @Override
    public void run(String... args) {
        if (projetoRepository.count() == 0) {
            projetoRepository.save(new Projeto(
                    "Mapa de Projetos Academicos",
                    "Catalogo inicial para divulgar projetos, grupos e oportunidades do campus.",
                    "Extensao",
                    "Equipe NEXUS HUB"
            ));
        }

        if (grupoRepository.count() == 0) {
            grupoRepository.save(new Grupo(
                    "Laboratorio de Ideias",
                    "Grupo piloto para iniciativas de ensino, pesquisa e extensao.",
                    "Inovacao",
                    "Equipe NEXUS HUB"
            ));
        }

        if (oportunidadeRepository.count() == 0) {
            oportunidadeRepository.save(new Oportunidade(
                    "Selecao de membros para projeto piloto",
                    "Chamada para estudantes interessados em testar e evoluir o NEXUS HUB.",
                    "Voluntariado",
                    "nexushub@exemplo.com",
                    LocalDate.now().plusDays(30)
            ));
        }
    }
}
