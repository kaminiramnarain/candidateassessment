package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReviewerDto {

    private UUID id;
    private String firstName;
    private String lastName;
}
