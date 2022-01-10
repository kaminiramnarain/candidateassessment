package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateTimeDto {

    private UUID userQuestionnaireId;
    private Integer remainingTime;
}
