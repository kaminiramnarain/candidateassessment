package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.CreateQuestionnaireQuestionDto;
import ch.elca.candidateassess.dto.QuestionnaireDataForReviewDto;

import java.util.List;

public interface QuestionnaireQuestionService {

    void saveQuestionnaireQuestion(CreateQuestionnaireQuestionDto createQuestionnaireQuestionDto);

    List<QuestionnaireDataForReviewDto> getQuestionsForReview(String userQuestionnaireId);

}
