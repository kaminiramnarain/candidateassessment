package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.CreateQuestionnaireQuestionDto;
import ch.elca.candidateassess.dto.FilledQuestionDto;
import ch.elca.candidateassess.dto.QuestionnaireDataForReviewDto;
import ch.elca.candidateassess.persistence.entity.Question;
import ch.elca.candidateassess.persistence.entity.QuestionnaireQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface QuestionnaireQuestionMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "questionnaire", ignore = true)
    QuestionnaireQuestion mapToQuestionnaireQuestion(CreateQuestionnaireQuestionDto createQuestionnaireQuestionDto);

    @Mapping(target = "candidateAnswerId", ignore = true)
    @Mapping(target = "marksAllocated", ignore = true)
    @Mapping(target = "textAnswer", ignore = true)
    QuestionnaireDataForReviewDto mapToQuestionnaireDataForReviewDto(QuestionnaireQuestion questionnaireQuestion);

<<<<<<< HEAD
=======
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "answerText", ignore = true)
    @Mapping(target = "marks", source = "questionnaireQuestion.question.marks")
    @Mapping(target = "marksObtained", ignore = true)
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
    @Mapping(target = "question", source = "questionnaireQuestion.question.questionEnglish")
    @Mapping(target = "questionType", source = "questionnaireQuestion.question.type")
    @Mapping(target = "skillName", source = "questionnaireQuestion.question.skill.name")
    @Mapping(target = "skillLevel", source = "questionnaireQuestion.question.skillLevel")
<<<<<<< HEAD
    @Mapping(target = "marks", source = "questionnaireQuestion.question.marks")
    FilledQuestionDto mapToFilledQuestionDto (QuestionnaireQuestion questionnaireQuestion);
}
=======
    FilledQuestionDto mapToFilledQuestionDto(QuestionnaireQuestion questionnaireQuestion);

}
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
