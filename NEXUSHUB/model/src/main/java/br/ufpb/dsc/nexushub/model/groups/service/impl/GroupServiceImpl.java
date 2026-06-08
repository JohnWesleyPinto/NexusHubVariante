package br.ufpb.dsc.nexushub.model.groups.service.impl;

import br.ufpb.dsc.nexushub.model.groups.domain.Group;
import br.ufpb.dsc.nexushub.model.groups.domain.GroupHumanMember;
import br.ufpb.dsc.nexushub.model.dto.GrupoRequest;
import br.ufpb.dsc.nexushub.model.groups.repository.GroupHumanMemberRepository;
import br.ufpb.dsc.nexushub.model.groups.repository.GroupRepository;
import br.ufpb.dsc.nexushub.model.groups.service.GroupService;
import br.ufpb.dsc.nexushub.model.identity.service.IdentityService;
import br.ufpb.dsc.nexushub.model.people.domain.Human;
import br.ufpb.dsc.nexushub.model.people.repository.HumanRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final HumanRepository humanRepository;
    private final GroupHumanMemberRepository memberRepository;
    private final IdentityService identityService;

    public GroupServiceImpl(
            GroupRepository groupRepository,
            HumanRepository humanRepository,
            GroupHumanMemberRepository memberRepository,
            IdentityService identityService
    ) {
        this.groupRepository = groupRepository;
        this.humanRepository = humanRepository;
        this.memberRepository = memberRepository;
        this.identityService = identityService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> listActiveGroups() {
        return groupRepository.findAll().stream()
                .filter(group -> group.getRecordStatus() == 1)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Group getGroup(UUID groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo nao encontrado."));
    }

    @Override
    @Transactional
    public Group createGroup(String name, String description, Integer type, UUID creatorHumanId, UUID updatedById) {
        UUID updater = updatedById == null ? identityService.firstUser().getId() : updatedById;
        Group group = groupRepository.save(new Group(name, description, type == null ? 1 : type, "#1e3a8a", null, updater));
        if (creatorHumanId != null) {
            addHumanToGroup(group.getId(), creatorHumanId, true, updater);
        }
        return group;
    }

    @Override
    @Transactional
    public Group createGroup(GrupoRequest request) {
        UUID updater = identityService.firstUser().getId();
        Group group = groupRepository.save(new Group(
                request.nome(),
                request.descricao(),
                parseGroupType(request.area()),
                parseGroupStatus(request.tipo()),
                request.cor() == null || request.cor().isBlank() ? "#1e3a8a" : request.cor(),
                request.logo(),
                updater
        ));
        if (request.criadorId() != null) {
            addHumanToGroup(group.getId(), request.criadorId(), true, updater);
        }
        return group;
    }

    @Override
    @Transactional
    public GroupHumanMember addHumanToGroup(UUID groupId, UUID humanId, boolean admin, UUID updatedById) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo nao encontrado."));
        Human human = humanRepository.findById(humanId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa nao encontrada."));
        return memberRepository.findByGroupAndHuman(group, human)
                .orElseGet(() -> memberRepository.save(new GroupHumanMember(group, human, admin, updatedById)));
    }

    @Override
    @Transactional
    public void deleteGroup(UUID groupId) {
        groupRepository.delete(getGroup(groupId));
    }

    private Integer parseType(String value, Integer fallback) {
        try {
            return value == null || value.isBlank() ? fallback : Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    private Integer parseGroupType(String value) {
        if (value == null || value.isBlank()) {
            return 1;
        }
        return switch (value.trim().toLowerCase()) {
            case "comunidade" -> 2;
            case "externo" -> 3;
            default -> 1;
        };
    }

    private Integer parseGroupStatus(String value) {
        if (value == null || value.isBlank()) {
            return 1;
        }
        return "restrito".equalsIgnoreCase(value.trim()) ? 2 : 1;
    }
}
