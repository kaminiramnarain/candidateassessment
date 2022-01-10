package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.entity.Skill;
import ch.elca.candidateassess.persistence.entity.UserQuestionnaire;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class UserQuestionnaireSkillDto {

    private UUID id;
    private UserQuestionnaire userQuestionnaire;
    private Skill skill;
    private SkillLevelEnum level;

}