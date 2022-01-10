package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.*;

import java.util.List;
import java.util.UUID;

public interface PublicService {

    ValidateTokenDto validateToken(String token);

    void validateId(UUID userQuestionnaireId);

    Double getMarksByUserQuestionnaireId(UUID userQuestionnaireId);

    Boolean getQuestionnaireType(UUID userQuestionnaireId);

    List<SkillDto> searchSkills(String name);

    void saveUserQuestionnaireSkill(CreateUserQuestionnaireSkillDto createUserQuestionnaireSkillDto);

    ValidateTokenDto getCandidateSelectSkillsStatus(String userQuestionnaireId);

    void generateQuestionnaire(UUID userQuestionnaireId);

    QuestionDto getQuestion(UUID userQuestionnaireId, Integer questionNumber);

    void updateCheatCount(UUID userQuestionnaireId);

    Integer getCheatCount(UUID userQuestionnaireId);

    void disqualifyQuestionnaire(UUID userQuestionnaireId, Integer remainingTime);

    void updateRemainingTime(UpdateTimeDto updateTimeDto);

    QuestionnaireDataDto getQuestionnaireData(UUID userQuestionnaireId);

    void saveCandidateAnswer(CreateCandidateAnswerDto createCandidateAnswerDto);

    void finishQuestionnaire(UUID userQuestionnaireId, Integer remainingTime);
}
