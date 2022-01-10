package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.*;

import java.util.UUID;

public interface UserQuestionnaireService {

    UserQuestionnaireDto saveUserQuestionnaire(CreateUserQuestionnaireDto createUserQuestionnaireDto);

    void updateUserQuestionnaire(UpdateUserQuestionnaireDto updateUserQuestionnaireDto, String userQuestionnaireId);

<<<<<<< HEAD
    void inviteCandidate(InviteCandidateDto candidateDto);

=======
    void updateRemainingTime(UpdateTimeDto updateTimeDto);

    void updateCheatCount(UUID userQuestionnaireId);

    Integer getCheatCount(UUID userQuestionnaireId);

    ValidateTokenDto validateToken(String token);

    void inviteCandidate(InviteCandidateDto candidateDto);

    Boolean getQuestionnaireType(UUID userQuestionnaireId);

    void disqualifyQuestionnaire(UUID userQuestionnaireId, Integer remainingTime);

    ValidateTokenDto getCandidateSelectSkillsStatus(String userQuestionnaireId);

>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
    void saveReviewedUserQuestionnaire(SaveReviewedUserQuestionnaireDto saveReviewedUserQuestionnaireDto, UUID userQuestionnaireId);

    void validateId(UUID userQuestionnaireId);

    void validateIdAndUnderReview(UUID userQuestionnaireId);
}