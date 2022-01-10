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
<<<<<<< HEAD
=======

    QuestionnaireDataDto getQuestionnaireData(UUID userQuestionnaireId);
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325

    FilledQuestionnaireDto getQuestionnaire(UUID userQuestionnaireId);
}