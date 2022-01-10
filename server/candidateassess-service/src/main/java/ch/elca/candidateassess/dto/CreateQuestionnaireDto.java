package ch.elca.candidateassess.dto;

import lombok.Data;

@Data
public class CreateQuestionnaireDto {

    private Integer marks;
    private Integer totalTime;
    private String templateName;
}
