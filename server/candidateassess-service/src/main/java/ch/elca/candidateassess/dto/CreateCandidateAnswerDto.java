package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateCandidateAnswerDto {

    private UUID userQuestionnaireId;
    private UUID questionId;
    private QuestionTypeEnum questionType;
    private String textAnswer;
    private List<MultipleAnswerDto> multipleAnswers;

}