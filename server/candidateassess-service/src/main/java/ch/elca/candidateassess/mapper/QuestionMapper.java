package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.persistence.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface QuestionMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questionEnglishFiltered", ignore = true)
    @Mapping(target = "skill", ignore = true)
    Question mapToQuestion(CreateQuestionDto createQuestionDto);

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "questionEnglishFiltered", ignore = true)
    @Mapping(target = "questionFrench", ignore = true)
    @Mapping(target = "questionStatus", ignore = true)
    @Mapping(target = "skillLevel", source = "level")
    @Mapping(target = "timeAssignedForQuestion", source = "time")
    @Mapping(target = "type", source = "questionType")
    Question mapToQuestionFromEditQuestionDto(EditQuestionDto editQuestionDto);

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "questionEnglishFiltered", ignore = true)
    @Mapping(target = "questionStatus", ignore = true)
    @Mapping(target = "skill.id", source = "newQuestionDto.skillId")
    @Mapping(target = "skillLevel", source = "newQuestionDto.level")
    @Mapping(target = "timeAssignedForQuestion", source = "newQuestionDto.time")
    @Mapping(target = "type", source = "newQuestionDto.questionType")
    Question mapToNewQuestion(NewQuestionDto newQuestionDto);

    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "level", source = "question.skillLevel")
    @Mapping(target = "questionType", source = "question.type")
    @Mapping(target = "time", source = "question.timeAssignedForQuestion")
    EditQuestionDto mapToEditQuestionDto(Question question);

    @Mapping(target = "answerText", ignore = true)
    @Mapping(target = "multipleAnswer", ignore = true)
    @Mapping(target = "possibleAnswers", ignore = true)
    @Mapping(target = "questionNumber", ignore = true)
    QuestionDto mapToQuestionDto(Question question);

    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "level", source = "question.skillLevel")
    @Mapping(target = "questionType", source = "question.type")
    @Mapping(target = "time", source = "question.timeAssignedForQuestion")
    ViewQuestionDto mapToViewQuestionDto(Question question);

}