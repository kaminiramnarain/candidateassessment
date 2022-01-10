package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MultipleAnswerDto {
    private UUID id;
    private String answerEnglish;
    private String answerFrench;
    private Boolean value;
}
