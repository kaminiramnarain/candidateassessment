package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class QuestionnaireQuestionDto {

    private UUID id;
    private UUID questionId;
    private UUID questionnaireId;
    private Integer questionNumber;

}
