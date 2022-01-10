package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class QuestionnaireDataForReviewDto {
    private UUID candidateAnswerId;
    private Integer questionNumber;
    private QuestionForReviewDto question;
    private String textAnswer;
    private Double marksAllocated;
}
