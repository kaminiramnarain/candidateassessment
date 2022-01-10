package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserQuestionnaireDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private Integer marks;
    private Integer timeTakenToCompleteQuestionnaire;
    private Integer remainingTime;
    private String token;
    private LocalDateTime interviewDate;
    private QuestionnaireStatusEnum status;
    private UUID questionnaireId;
    private UUID positionId;

}
