package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.CreateCandidateAnswerDto;
import ch.elca.candidateassess.dto.SaveCandidateMarksDto;

import java.util.UUID;

public interface CandidateAnswerService {

    void saveCandidateAnswer(CreateCandidateAnswerDto createCandidateAnswerDto);

    void saveCandidateMarks(SaveCandidateMarksDto saveCandidateMarksDto);

    void finishQuestionnaire(UUID userQuestionnaireId, Integer remainingTime);
}