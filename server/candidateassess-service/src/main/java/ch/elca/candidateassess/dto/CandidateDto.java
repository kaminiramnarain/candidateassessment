package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CandidateDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private Integer remainingTime;
    private PositionDto position;
    private Boolean userArchived;
    private QuestionnaireStatusEnum questionnaireStatus;
    private List<SelectedSkillDto> skills;
    private List<ReviewerDto> reviewers;

}