package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateQuestionnaireQuestionDto {

    private UUID questionId;
    private UUID questionnaireId;
    private Integer questionNumber;

}