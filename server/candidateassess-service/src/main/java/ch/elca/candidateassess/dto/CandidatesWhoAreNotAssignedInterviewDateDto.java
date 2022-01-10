package ch.elca.candidateassess.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CandidatesWhoAreNotAssignedInterviewDateDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private LocalDateTime interviewDate;
}
