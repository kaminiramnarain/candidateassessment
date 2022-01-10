package ch.elca.candidateassess.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveQuestionDto {
    private List<NewQuestionDto> questions;
}
