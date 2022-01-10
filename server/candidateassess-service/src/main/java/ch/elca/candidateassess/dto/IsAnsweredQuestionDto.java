package ch.elca.candidateassess.dto;

import lombok.Data;

@Data
public class IsAnsweredQuestionDto {
    private Integer questionNumber;
    private boolean isAnswered;
}
