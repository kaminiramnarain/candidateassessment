package ch.elca.candidateassess.dto;

import lombok.Data;

@Data
public class FilledAnswerDto {
    private String answerEnglish;
    private Boolean selected;
    private Boolean valid;
}
