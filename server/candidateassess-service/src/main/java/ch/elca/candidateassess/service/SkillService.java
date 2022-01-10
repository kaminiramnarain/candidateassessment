package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.CreateSkillDto;
import ch.elca.candidateassess.dto.SkillDto;

import java.util.List;

public interface SkillService {

    void saveSkill(CreateSkillDto createSkillDto);

    List<SkillDto> searchSkills(String name);

}