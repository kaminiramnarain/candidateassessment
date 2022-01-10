package ch.elca.candidateassess.mapper;

<<<<<<< HEAD
import ch.elca.candidateassess.dto.*;
=======
import ch.elca.candidateassess.dto.AnswerDto;
import ch.elca.candidateassess.dto.FilledAnswerDto;
import ch.elca.candidateassess.dto.MultipleAnswerDto;
import ch.elca.candidateassess.dto.PossibleAnswerDto;
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
import ch.elca.candidateassess.persistence.entity.Answer;
import ch.elca.candidateassess.persistence.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface AnswerMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question.id", source = "questionId")
    Answer mapToAnswer(AnswerDto answerDto);

    @Mapping(target = "questionId", source = "answer.question.id")
    AnswerDto mapToAnswerDto(Answer answer);

<<<<<<< HEAD

=======
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "question", ignore = true)
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
    @Mapping(target = "valid", source = "multipleAnswerDto.value")
    Answer mapToAnswerFromNewQuestionDto(MultipleAnswerDto multipleAnswerDto);

    PossibleAnswerDto mapToPossibleAnswerDto(Answer answer);

<<<<<<< HEAD
    MultipleAnswerDto mapToMultipleAnswerDto(Answer answer);

=======
    @Mapping(target = "value", ignore = true)
    MultipleAnswerDto mapToMultipleAnswerDto(Answer answer);

    @Mapping(target = "selected", ignore = true)
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
    FilledAnswerDto mapToFilledAnswerDto(Answer answer);

}