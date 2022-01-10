package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.CandidateReviewDto;
import ch.elca.candidateassess.dto.CreateReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;


public interface ReviewService {

    void saveReview(CreateReviewDto createReviewDto);

    Page<CandidateReviewDto> getUserQuestionnaireData(Sort sort, Integer pageNumber, Integer pageSize, String personEmail);

    Page<CandidateReviewDto> searchByCandidateName(String candidateName,Sort sort, Integer pageNumber, Integer pageSize, String personEmail);
}

