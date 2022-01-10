package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class QuestionDto {

    @NotNull
    private UUID id;
    private String questionEnglish;
    private String questionFrench;
    private Integer questionNumber;
    private QuestionTypeEnum type;
    private Double marks;
    private List<PossibleAnswerDto> possibleAnswers;
    private String answerText;
    private List<MultipleAnswerDto> multipleAnswer;
}