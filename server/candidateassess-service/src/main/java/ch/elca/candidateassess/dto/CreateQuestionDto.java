package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.QuestionStatusEnum;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CreateQuestionDto {

    private String questionEnglish;
    private String questionFrench;
    private String image;
    private QuestionStatusEnum questionStatus;
    private QuestionTypeEnum type;
    private Double marks;
    private Integer timeAssignedForQuestion;
    private SkillLevelEnum skillLevel;
    @NotNull
    private UUID skillId;

}