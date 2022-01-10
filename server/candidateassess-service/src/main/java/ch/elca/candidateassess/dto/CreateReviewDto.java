package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.ReviewStatusEnum;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateReviewDto {

    private List<String> personIds;
    private ReviewStatusEnum status;
    private UUID userQuestionnaireId;

}
