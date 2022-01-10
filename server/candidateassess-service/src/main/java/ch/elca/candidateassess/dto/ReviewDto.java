package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.ReviewStatusEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewDto {

    private UUID id;
    private UUID personId;
    private String comment;
    private ReviewStatusEnum status;
    private UUID userQuestionnaireId;

}