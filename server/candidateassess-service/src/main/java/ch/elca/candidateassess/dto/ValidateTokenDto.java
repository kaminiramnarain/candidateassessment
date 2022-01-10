package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ValidateTokenDto {
    private Boolean candidateSelectSkills;
    private Boolean isAttempted;
    private UUID userQuestionnaireId;
}
