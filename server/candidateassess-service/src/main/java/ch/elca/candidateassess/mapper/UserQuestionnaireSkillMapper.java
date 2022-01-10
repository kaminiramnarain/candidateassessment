package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.SelectedSkillDto;
import ch.elca.candidateassess.dto.SkillLevelDto;
import ch.elca.candidateassess.persistence.entity.UserQuestionnaireSkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface UserQuestionnaireSkillMapper {

    @Mapping(target = "name", source = "userQuestionnaireSkill.skill.name")
    @Mapping(target = "id", source = "userQuestionnaireSkill.skill.id")
    SelectedSkillDto mapToSelectedSkillDto(UserQuestionnaireSkill userQuestionnaireSkill);

    SkillLevelDto mapToSkillLevelDto(UserQuestionnaireSkill userQuestionnaireSkill);

}