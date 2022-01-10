package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.List;

@Data
public class FilledQuestionnaireDto {
   private List<FilledQuestionDto> questions;
   private String firstName;
   private String lastName;
   private Double marksObtained;
   private Double marks;
   private Integer length;
   private Integer timeTaken;
}
