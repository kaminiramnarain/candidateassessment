package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateUserQuestionnaireDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private Integer marks;
    private Boolean autoGenerate;
    private Integer remainingTime;
    private Integer timeTakenToCompleteQuestionnaire;
    private Boolean candidateSelectSkills;
    private String token;
    private LocalDateTime interviewDate;
    private QuestionnaireStatusEnum status;
    private UUID questionnaireId;
    private UUID positionId;

}
