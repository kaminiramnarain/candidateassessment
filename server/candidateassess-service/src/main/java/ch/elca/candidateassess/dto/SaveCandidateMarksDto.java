package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SaveCandidateMarksDto {
    private UUID candidateAnswerId;
    private Double marksAllocated;
}
