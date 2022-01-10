package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateUserQuestionnaireSkillDto {

    private UUID userQuestionnaireId;
    private List<SelectedSkillDto> skills;

}