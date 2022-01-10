package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireDataDto {

    private List<IsAnsweredQuestionDto> questions;
    private Integer remainingTime;
    private Integer duration;
    private boolean questionnaireOpen;
}
