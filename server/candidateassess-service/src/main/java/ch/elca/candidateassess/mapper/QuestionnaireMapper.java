package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.CreateQuestionnaireDto;
import ch.elca.candidateassess.dto.QuestionForReviewDto;
import ch.elca.candidateassess.dto.QuestionnaireDto;
import ch.elca.candidateassess.persistence.entity.Questionnaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface QuestionnaireMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    Questionnaire mapToQuestionnaire(CreateQuestionnaireDto createQuestionnaireDto);

    QuestionnaireDto mapToQuestionnaireDto(Questionnaire questionnaire);

}