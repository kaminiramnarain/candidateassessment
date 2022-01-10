package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.CreateCandidateAnswerDto;
import ch.elca.candidateassess.dto.MultipleAnswerDto;
import ch.elca.candidateassess.dto.SaveCandidateMarksDto;
import ch.elca.candidateassess.mapper.CandidateAnswerMapper;
import ch.elca.candidateassess.persistence.entity.Answer;
import ch.elca.candidateassess.persistence.entity.CandidateAnswer;
import ch.elca.candidateassess.persistence.entity.Questionnaire;
import ch.elca.candidateassess.persistence.entity.UserQuestionnaire;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import ch.elca.candidateassess.persistence.repository.*;
import ch.elca.candidateassess.service.CandidateAnswerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateAnswerServiceImpl implements CandidateAnswerService {

    private final CandidateAnswerRepository candidateAnswerRepository;
    private final UserQuestionnaireRepository userQuestionnaireRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final CandidateAnswerMapper candidateAnswerMapper;

    public CandidateAnswerServiceImpl(CandidateAnswerRepository candidateAnswerRepository, UserQuestionnaireRepository userQuestionnaireRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, QuestionnaireRepository questionnaireRepository, CandidateAnswerMapper candidateAnswerMapper) {
        this.candidateAnswerRepository = candidateAnswerRepository;
        this.userQuestionnaireRepository = userQuestionnaireRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.candidateAnswerMapper = candidateAnswerMapper;
    }

    @Override
    public void saveCandidateMarks(SaveCandidateMarksDto candidateMarksDto) {
        if (candidateMarksDto.getCandidateAnswerId() != null) {
            CandidateAnswer candidateAnswer = candidateAnswerRepository.getById(candidateMarksDto.getCandidateAnswerId());
            candidateAnswer.setMarksAllocated(candidateMarksDto.getMarksAllocated());
            candidateAnswerRepository.save(candidateAnswer);
        }
    }


}