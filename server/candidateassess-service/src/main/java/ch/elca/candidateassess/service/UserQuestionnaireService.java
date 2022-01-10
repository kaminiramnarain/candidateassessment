package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.*;

import java.util.UUID;

public interface UserQuestionnaireService {

    UserQuestionnaireDto saveUserQuestionnaire(CreateUserQuestionnaireDto createUserQuestionnaireDto);

    void updateUserQuestionnaire(UpdateUserQuestionnaireDto updateUserQuestionnaireDto, String userQuestionnaireId);

    void updateRemainingTime(UpdateTimeDto updateTimeDto);

    void updateCheatCount(UUID userQuestionnaireId);

    Integer getCheatCount(UUID userQuestionnaireId);

    ValidateTokenDto validateToken(String token);

    void inviteCandidate(InviteCandidateDto candidateDto);

    Boolean getQuestionnaireType(UUID userQuestionnaireId);

    void disqualifyQuestionnaire(UUID userQuestionnaireId, Integer remainingTime);

    ValidateTokenDto getCandidateSelectSkillsStatus(String userQuestionnaireId);

    void saveReviewedUserQuestionnaire(SaveReviewedUserQuestionnaireDto saveReviewedUserQuestionnaireDto, UUID userQuestionnaireId);

    Double getMarksByUserQuestionnaireId(UUID userQuestionnaireId);

    void validateId(UUID userQuestionnaireId);

    void validateIdAndUnderReview(UUID userQuestionnaireId);
}