package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.entity.Skill;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import lombok.Data;

@Data
public class SkillLevelDto {
    private Skill skill;
    private SkillLevelEnum level;
}
