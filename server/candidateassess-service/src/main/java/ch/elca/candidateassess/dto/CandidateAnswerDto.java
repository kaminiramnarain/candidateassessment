package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CandidateAnswerDto {

    private UUID id;
    private String textAnswer;
    private Boolean valid;
    private UUID userQuestionnaireId;
    private UUID questionId;
    private UUID answerId;

}