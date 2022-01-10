package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.CreateSkillDto;
import ch.elca.candidateassess.dto.SkillDto;
import ch.elca.candidateassess.mapper.SkillMapper;
import ch.elca.candidateassess.persistence.entity.QSkill;
import ch.elca.candidateassess.persistence.entity.Skill;
import ch.elca.candidateassess.persistence.repository.SkillRepository;
import ch.elca.candidateassess.service.SkillService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public SkillServiceImpl(SkillRepository skillRepository, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    @Override
    public List<SkillDto> searchSkills(String name) {
        BooleanBuilder searchPredicate = buildSearchPredicate(name);
        List<Skill> skillList = new ArrayList<>();
        skillRepository.findAll(searchPredicate).forEach(skillList::add);
        return skillList.stream().map(skillMapper::mapToSkillDto).collect(Collectors.toList());
    }

    @Override
    public void saveSkill(CreateSkillDto createSkillDto) {
        Skill skill = skillMapper.mapToSkill(createSkillDto);
        skillRepository.save(skill);
    }

    private BooleanBuilder buildSearchPredicate(String name) {
        var qSkill = QSkill.skill;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Objects.nonNull(name)) {
            booleanBuilder.and(qSkill.name.toLowerCase().contains(name.toLowerCase(Locale.ROOT)));
        }
        return booleanBuilder;
    }
}
