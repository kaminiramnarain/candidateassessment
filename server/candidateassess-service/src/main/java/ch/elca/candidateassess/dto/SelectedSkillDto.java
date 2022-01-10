package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class SelectedSkillDto {

    private UUID id;
    private String name;
    private SkillLevelEnum level;

}