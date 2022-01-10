package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.CreateQuestionnaireDto;
import ch.elca.candidateassess.dto.FilledQuestionnaireDto;
import ch.elca.candidateassess.dto.QuestionnaireDataDto;
import ch.elca.candidateassess.dto.QuestionnaireDto;

import java.util.List;
import java.util.UUID;

public interface QuestionnaireService {

    QuestionnaireDto saveQuestionnaire(CreateQuestionnaireDto createQuestionnaireDto);

    void generateQuestionnaire(UUID userQuestionnaireId);

    void customizeQuestionnaire(UUID userQuestionnaireId, List<String>idList);

    QuestionnaireDataDto getQuestionnaireData(UUID userQuestionnaireId);

    FilledQuestionnaireDto getQuestionnaire(UUID userQuestionnaireId);
}