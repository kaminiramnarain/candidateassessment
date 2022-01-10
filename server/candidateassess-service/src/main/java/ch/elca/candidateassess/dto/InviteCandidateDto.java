package ch.elca.candidateassess.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class InviteCandidateDto {

    private UUID id;
    private Date interviewDate;

}
