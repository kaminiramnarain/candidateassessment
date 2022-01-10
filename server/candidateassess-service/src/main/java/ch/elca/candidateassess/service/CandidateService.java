package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.CandidateDto;
import ch.elca.candidateassess.dto.CandidateReviewDto;
import ch.elca.candidateassess.dto.CandidatesWhoAreNotAssignedInterviewDateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CandidateService {

    Page<CandidateDto> getCandidates(Sort sort, Integer pageNumber, Integer pageSize);

    Page<CandidateDto> getCandidatesForCustomizedQuestionnaires(UUID personId, Sort sort, Integer pageNumber, Integer pageSize);

    Page<CandidateDto> searchCandidatesForCustomizedQuestionnaires(UUID personId, String candidateName, Sort sort, Integer pageNumber, Integer pageSize);

    Page<CandidateDto> findCandidatesByName(String candidateName, Sort sort, Integer pageNumber, Integer pageSize);

    void markCandidateAsArchived(String candidateId);


    List<CandidatesWhoAreNotAssignedInterviewDateDto> getCandidatesWhoAreNotAssignedInterviewDate();

    List<CandidatesWhoAreNotAssignedInterviewDateDto> findCandidatesWhoAreNotAssignedInterviewDateByName(String candidateName);

    Optional<CandidateDto> getCandidateById(String candidateId);

    CandidateReviewDto getCandidateDataForReview(String candidateId);
}
