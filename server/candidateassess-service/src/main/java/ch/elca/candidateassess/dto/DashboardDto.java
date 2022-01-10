package ch.elca.candidateassess.dto;

import lombok.Data;

@Data
public class DashboardDto {
    private String skillName;
    private Integer numberOfOpenEndedQuestions;
    private Integer numberOfMultipleChoiceQuestions;
    private Integer numberOfMultipleAnswersQuestions;
}
