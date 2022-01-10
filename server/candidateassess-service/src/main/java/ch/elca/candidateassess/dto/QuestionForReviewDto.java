package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class QuestionForReviewDto {
    private UUID id;
    private String questionEnglish;
    private String questionFrench;
    private Integer marks;

}
