package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.CreateCandidateAnswerDto;
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
    @Mapping(target = "questionNumber", ignore = true)
    void mapToQuestionnaireDataForReviewDto(@MappingTarget QuestionnaireDataForReviewDto questionnaireDataForReviewDto, CandidateAnswer candidateAnswer);

}