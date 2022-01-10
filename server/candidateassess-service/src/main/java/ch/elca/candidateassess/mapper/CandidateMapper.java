package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.CandidateDto;
import ch.elca.candidateassess.dto.CandidateReviewDto;
import ch.elca.candidateassess.dto.CandidatesWhoAreNotAssignedInterviewDateDto;
import ch.elca.candidateassess.persistence.entity.UserQuestionnaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface CandidateMapper {

    @Mapping(target = "reviewers", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(source = "status", target = "questionnaireStatus")
    CandidateDto mapToCandidateDto(UserQuestionnaire userQuestionnaire);

    CandidateReviewDto mapToCandidateReviewDto(UserQuestionnaire userQuestionnaire);

    CandidatesWhoAreNotAssignedInterviewDateDto mapToCandidatesWhoAreNotAssignedInterviewDateDto(UserQuestionnaire userQuestionnaire);

}