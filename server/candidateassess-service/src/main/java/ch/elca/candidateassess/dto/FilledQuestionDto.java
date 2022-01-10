package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import lombok.Data;

import java.util.List;

@Data
public class FilledQuestionDto {
    private Integer questionNumber;
    private String question;
    private String answerText;
    private List<FilledAnswerDto> answers;
    private QuestionTypeEnum questionType;
    private String skillName;
    private SkillLevelEnum skillLevel;
    private Double marksObtained;
    private Double marks;
}
