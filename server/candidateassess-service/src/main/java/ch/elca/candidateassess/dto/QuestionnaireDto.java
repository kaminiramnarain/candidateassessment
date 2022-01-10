package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class QuestionnaireDto {

    private UUID id;
    private Integer marks;
    private Integer totalTime;
    private String templateName;

}