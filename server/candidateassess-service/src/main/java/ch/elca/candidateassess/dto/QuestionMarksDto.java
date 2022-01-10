package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class QuestionMarksDto {
    private Integer marksAllocated;
    private UUID questionId;
}
