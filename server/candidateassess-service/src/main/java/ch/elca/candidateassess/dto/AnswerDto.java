package ch.elca.candidateassess.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class AnswerDto {

    @NotNull
    private UUID questionId;
    private String answerEnglish;
    private String answerFrench;
    private Boolean valid;

}