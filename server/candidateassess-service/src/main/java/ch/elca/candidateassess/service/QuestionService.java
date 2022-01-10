package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import ch.elca.candidateassess.dto.CreateQuestionDto;
import ch.elca.candidateassess.dto.QuestionDto;
import ch.elca.candidateassess.dto.SaveQuestionDto;

import javax.swing.text.View;
import java.util.List;
import java.util.UUID;

public interface QuestionService {

    void saveQuestion(CreateQuestionDto createQuestionDto);

    QuestionDto getQuestion(UUID userQuestionnaireId, Integer questionNumber);

    Page<ViewQuestionDto> getQuestions(Sort sort, Integer pageNumber, Integer pageSize);

    Page<ViewQuestionDto> findQuestionByContent(String questionContent, UUID selectedSkillLevelId, SkillLevelEnum selectedSkillLevel, QuestionTypeEnum selectedQuestionType, Sort sort, Integer pageNumber, Integer pageSize);

    void validateId(UUID questionId);

    void deleteQuestion(UUID questionId);

    void saveQuestions(SaveQuestionDto saveQuestionDto);

    EditQuestionDto getQuestionById(UUID questionId);

    void updateQuestion(UUID questionId, EditQuestionDto editQuestionDto);

    List<DashboardDto> getDashboardData();
}