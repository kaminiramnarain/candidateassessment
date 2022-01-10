package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.CreateSkillDto;
import ch.elca.candidateassess.dto.DashboardDto;
<<<<<<< HEAD
import ch.elca.candidateassess.dto.SelectedSkillDto;
=======
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
import ch.elca.candidateassess.dto.SkillDto;
import ch.elca.candidateassess.persistence.entity.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface SkillMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    Skill mapToSkill(CreateSkillDto createSkillDto);

    SkillDto mapToSkillDto(Skill skill);

<<<<<<< HEAD
    @Mapping(source = "skill.name" , target = "skillName")
    DashboardDto mapToDashboardDto(Skill skill);
=======
    @Mapping(target = "numberOfMultipleAnswersQuestions", ignore = true)
    @Mapping(target = "numberOfMultipleChoiceQuestions", ignore = true)
    @Mapping(target = "numberOfOpenEndedQuestions", ignore = true)
    @Mapping(source = "skill.name", target = "skillName")
    DashboardDto mapToDashboardDto(Skill skill);

>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
}