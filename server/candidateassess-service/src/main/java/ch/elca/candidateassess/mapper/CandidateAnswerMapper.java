package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.CreateCandidateAnswerDto;
<<<<<<< HEAD
import ch.elca.candidateassess.dto.LoginDto;
=======
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
import ch.elca.candidateassess.dto.QuestionnaireDataForReviewDto;
import ch.elca.candidateassess.persistence.entity.CandidateAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface CandidateAnswerMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answer", ignore = true)
    @Mapping(target = "marksAllocated", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "userQuestionnaire", ignore = true)
    CandidateAnswer mapToCandidateAnswer(CreateCandidateAnswerDto createCandidateAnswerDto);

    @Mapping(target = "candidateAnswerId", source = "candidateAnswer.id")
<<<<<<< HEAD
    QuestionnaireDataForReviewDto mapToQuestionnaireDataForReviewDto(@MappingTarget QuestionnaireDataForReviewDto questionnaireDataForReviewDto, CandidateAnswer candidateAnswer);
}
=======
    @Mapping(target = "questionNumber", ignore = true)
    void mapToQuestionnaireDataForReviewDto(@MappingTarget QuestionnaireDataForReviewDto questionnaireDataForReviewDto, CandidateAnswer candidateAnswer);

}
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
