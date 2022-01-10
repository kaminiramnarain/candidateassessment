package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.CreateQuestionnaireQuestionDto;
import ch.elca.candidateassess.dto.QuestionnaireDataForReviewDto;
import ch.elca.candidateassess.exception.ResourceNotFoundException;
import ch.elca.candidateassess.mapper.CandidateAnswerMapper;
import ch.elca.candidateassess.mapper.QuestionnaireQuestionMapper;
import ch.elca.candidateassess.mapper.UUIDMapper;
import ch.elca.candidateassess.persistence.entity.*;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.repository.*;
import ch.elca.candidateassess.service.QuestionnaireQuestionService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionnaireQuestionServiceImpl implements QuestionnaireQuestionService {


    private final CandidateAnswerRepository candidateAnswerRepository;
    private final CandidateAnswerMapper candidateAnswerMapper;
    private final UserQuestionnaireRepository userQuestionnaireRepository;
    private final QuestionRepository questionRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionnaireQuestionRepository questionnaireQuestionRepository;
    private final QuestionnaireQuestionMapper questionnaireQuestionMapper;
    private final UUIDMapper uuidMapper;


    public QuestionnaireQuestionServiceImpl(CandidateAnswerRepository candidateAnswerRepository, CandidateAnswerMapper candidateAnswerMapper, UserQuestionnaireRepository userQuestionnaireRepository, QuestionRepository questionRepository, QuestionnaireRepository questionnaireRepository, QuestionnaireQuestionRepository questionnaireQuestionRepository, QuestionnaireQuestionMapper questionnaireQuestionMapper, UUIDMapper uuidMapper) {

        this.candidateAnswerRepository = candidateAnswerRepository;
        this.candidateAnswerMapper = candidateAnswerMapper;
        this.userQuestionnaireRepository = userQuestionnaireRepository;
        this.questionRepository = questionRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireQuestionRepository = questionnaireQuestionRepository;
        this.questionnaireQuestionMapper = questionnaireQuestionMapper;
        this.uuidMapper = uuidMapper;
    }

    @Override
    public void saveQuestionnaireQuestion(CreateQuestionnaireQuestionDto createQuestionnaireQuestionDto) {
        QuestionnaireQuestion questionnaireQuestion = questionnaireQuestionMapper.mapToQuestionnaireQuestion(createQuestionnaireQuestionDto);
        questionRepository.findById(createQuestionnaireQuestionDto.getQuestionId()).ifPresentOrElse(questionnaireQuestion::setQuestion, () -> {
            throw new ResourceNotFoundException("Question does not exist!");
        });
        questionnaireRepository.findById(createQuestionnaireQuestionDto.getQuestionnaireId()).ifPresentOrElse(questionnaireQuestion::setQuestionnaire, () -> {
            throw new ResourceNotFoundException("Questionnaire does not exist!");
        });
        questionnaireQuestionRepository.save(questionnaireQuestion);
    }


    @Override
    public List<QuestionnaireDataForReviewDto> getQuestionsForReview(String userQuestionnaireId) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(uuidMapper.mapToUUID(userQuestionnaireId));
        UUID questionnaireId = userQuestionnaire.getQuestionnaire().getId();
        BooleanBuilder questionPredicate = buildQuestionsPredicate(questionnaireId);
        List<QuestionnaireQuestion> questions = new ArrayList<QuestionnaireQuestion>();
        questionnaireQuestionRepository.findAll(questionPredicate).forEach(questions::add);
        return questions.stream()
                .map(questionnaireQuestionMapper::mapToQuestionnaireDataForReviewDto)
                .map(questionnaireDataForReviewDto -> {
                    BooleanBuilder answerPredicate = buildAnswersPredicate(questionnaireDataForReviewDto.getQuestion().getId());
                    List<CandidateAnswer> candidateAnswers = new ArrayList<CandidateAnswer>();
                    candidateAnswerRepository.findAll(answerPredicate).forEach(candidateAnswers::add);
                    candidateAnswers.forEach(candidateAnswer -> {
                        candidateAnswerMapper.mapToQuestionnaireDataForReviewDto(questionnaireDataForReviewDto, candidateAnswer);
                    });
                    return questionnaireDataForReviewDto;
                })
                .collect(Collectors.toList());
    }

    private BooleanBuilder buildQuestionsPredicate(UUID questionnaireId) {
        var qQuestionnaireQuestion = QQuestionnaireQuestion.questionnaireQuestion;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qQuestionnaireQuestion.questionnaire.id.eq(questionnaireId)).and(qQuestionnaireQuestion.question.type.eq(QuestionTypeEnum.OPEN_ENDED));
    }

    private BooleanBuilder buildAnswersPredicate(UUID questionnId) {
        var qCandidateAnswer = QCandidateAnswer.candidateAnswer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qCandidateAnswer.question.id.eq(questionnId));
    }


}
