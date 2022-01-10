package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.entity.Skill;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EditQuestionDto {
        private UUID id;
        private String questionEnglish;
        private QuestionTypeEnum questionType;
        private Skill skill;
        private SkillLevelEnum level;
        private Double marks;
        private Integer time;
        private List<AnswerDto> answers;
}
