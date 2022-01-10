package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class NewQuestionDto {
    private UUID id;
    private UUID skillId;
    private SkillLevelEnum level;
    private QuestionTypeEnum questionType;
    private Double marks;
    private Integer time;
    private String questionEnglish;
    private String questionFrench;
    private List<MultipleAnswerDto> multipleAnswers;
}
