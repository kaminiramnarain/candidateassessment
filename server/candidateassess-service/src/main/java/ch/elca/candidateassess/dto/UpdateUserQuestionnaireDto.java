package ch.elca.candidateassess.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class UpdateUserQuestionnaireDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private UUID positionId;
}
