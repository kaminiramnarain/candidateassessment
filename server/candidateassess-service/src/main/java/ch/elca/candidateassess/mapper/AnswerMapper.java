package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.AnswerDto;
import ch.elca.candidateassess.dto.FilledAnswerDto;
import ch.elca.candidateassess.dto.MultipleAnswerDto;
import ch.elca.candidateassess.dto.PossibleAnswerDto;
import ch.elca.candidateassess.persistence.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface AnswerMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "questionId", target = "question.id")
    Answer mapToAnswer(AnswerDto answerDto);

    @Mapping(target = "questionId", source = "answer.question.id")
    AnswerDto mapToAnswerDto(Answer answer);

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "valid", source = "multipleAnswerDto.value")
    Answer mapToAnswerFromNewQuestionDto(MultipleAnswerDto multipleAnswerDto);

    PossibleAnswerDto mapToPossibleAnswerDto(Answer answer);

    @Mapping(target = "value", ignore = true)
    MultipleAnswerDto mapToMultipleAnswerDto(Answer answer);

    @Mapping(target = "selected", ignore = true)
    FilledAnswerDto mapToFilledAnswerDto(Answer answer);

}