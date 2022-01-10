package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.exception.ResourceNotFoundException;
import ch.elca.candidateassess.mapper.AnswerMapper;
import ch.elca.candidateassess.mapper.QuestionMapper;
import ch.elca.candidateassess.mapper.SkillMapper;
import ch.elca.candidateassess.persistence.entity.*;
import ch.elca.candidateassess.persistence.enumeration.QuestionStatusEnum;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import ch.elca.candidateassess.persistence.repository.*;
import ch.elca.candidateassess.service.QuestionService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final SkillMapper skillMapper;
    private final AnswerMapper answerMapper;
    private final SkillRepository skillRepository;
    private final UserQuestionnaireRepository userQuestionnaireRepository;
    private final QuestionnaireQuestionRepository questionnaireQuestionRepository;
    private final CandidateAnswerRepository candidateAnswerRepository;
    private final AnswerRepository answerRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper, SkillMapper skillMapper, AnswerMapper answerMapper, SkillRepository skillRepository, UserQuestionnaireRepository userQuestionnaireRepository, QuestionnaireQuestionRepository questionnaireQuestionRepository, CandidateAnswerRepository candidateAnswerRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.skillMapper = skillMapper;
        this.answerMapper = answerMapper;
        this.skillRepository = skillRepository;
        this.userQuestionnaireRepository = userQuestionnaireRepository;
        this.questionnaireQuestionRepository = questionnaireQuestionRepository;
        this.candidateAnswerRepository = candidateAnswerRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public void saveQuestion(CreateQuestionDto createQuestionDto) {
        Question question = questionMapper.mapToQuestion(createQuestionDto);
        skillRepository.findById(createQuestionDto.getSkillId()).ifPresentOrElse(question::setSkill, () -> {
            throw new ResourceNotFoundException("Skill does not exist!");
        });
        questionRepository.save(question);
    }

    @Override
    public void validateId(UUID questionId) {
        questionRepository.findById(questionId).ifPresentOrElse(question -> {
        }, () -> {
            throw new ResourceNotFoundException("Question does not exist!");
        });
    }

    @Override
    public void saveQuestions(SaveQuestionDto saveQuestionDto) {
        saveQuestionDto.getQuestions().forEach(newQuestion -> {
            Question question = questionMapper.mapToNewQuestion(newQuestion);
            question.setQuestionStatus(QuestionStatusEnum.VALID);
            var savedQuestion = questionRepository.save(question);
            if (newQuestion.getQuestionType().equals(QuestionTypeEnum.MULTIPLE_CHOICE) || newQuestion.getQuestionType().equals(QuestionTypeEnum.MULTIPLE_ANSWERS)) {
                newQuestion.getMultipleAnswers().forEach(newAnswer -> {
                    Answer answer = answerMapper.mapToAnswerFromNewQuestionDto(newAnswer);
                    answer.setQuestion(savedQuestion);
                    answerRepository.save(answer);
                });
            }
        });
    }

    private BooleanBuilder buildAnswerPredicate(UUID questionId) {
        var qAnswer = QAnswer.answer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qAnswer.question.id.eq(questionId));
    }

    @Override
    public EditQuestionDto getQuestionById(UUID questionId) {
        Question question = questionRepository.getById(questionId);
        BooleanBuilder answerPredicate = buildAnswerPredicate(questionId);
        List<Answer> answerList = new ArrayList<Answer>();
        answerRepository.findAll(answerPredicate).forEach(answerList::add);
        List<AnswerDto> answers = answerList.stream().map(answerMapper::mapToAnswerDto).collect(Collectors.toList());
        EditQuestionDto editQuestionDto = questionMapper.mapToEditQuestionDto(question);
        editQuestionDto.setAnswers(answers);
        return editQuestionDto;
    }

    @Override
    public void updateQuestion(UUID questionId, EditQuestionDto editQuestionDto) {
        questionRepository.findById(questionId).ifPresentOrElse((question) -> {
            Question updateQuestion = questionMapper.mapToQuestionFromEditQuestionDto(editQuestionDto);
            updateQuestion.setQuestionStatus(QuestionStatusEnum.VALID);
            questionRepository.save(updateQuestion);
        }, () -> {
        });
        if (editQuestionDto.getQuestionType().equals(QuestionTypeEnum.MULTIPLE_CHOICE) || editQuestionDto.getQuestionType().equals(QuestionTypeEnum.MULTIPLE_ANSWERS)) {
            BooleanBuilder answerPredicate = buildAnswerPredicate(questionId);
            List<Answer> existingAnswers = new ArrayList<Answer>();
            answerRepository.findAll(answerPredicate).forEach(existingAnswers::add);
            List<Answer> newAnswers = new ArrayList<>();
            answerRepository.deleteAll(existingAnswers);
            Question question = questionRepository.getById(editQuestionDto.getId());
            editQuestionDto.getAnswers().forEach(answer -> {
                Answer newAnswer = answerMapper.mapToAnswer(answer);
                newAnswer.setQuestion(question);
                newAnswers.add(newAnswer);
            });
            answerRepository.saveAll(newAnswers);
        }
    }

    private BooleanBuilder buildQuestionnaireQuestionPredicate(Integer questionNumber, UUID questionnaireId) {
        var qQuestionnaireQuestion = QQuestionnaireQuestion.questionnaireQuestion;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qQuestionnaireQuestion.questionnaire.id.eq(questionnaireId)).and(qQuestionnaireQuestion.questionNumber.eq(questionNumber));
        return booleanBuilder;
    }

    private BooleanBuilder buildCandidateAnswerPredicate(UUID questionId, UUID userQuestionnaireId) {
        var qCandidateAnswer = QCandidateAnswer.candidateAnswer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qCandidateAnswer.userQuestionnaire.id.eq(userQuestionnaireId)).and(qCandidateAnswer.question.id.eq(questionId));
        return booleanBuilder;
    }

    @Override
    public QuestionDto getQuestion(UUID userQuestionnaireId, Integer questionNumber) {
        UUID questionnaireId = userQuestionnaireRepository.getById(userQuestionnaireId).getQuestionnaire().getId();
        BooleanBuilder questionnaireQuestionPredicate = buildQuestionnaireQuestionPredicate(questionNumber, questionnaireId);
        List<QuestionnaireQuestion> questionList = new ArrayList<QuestionnaireQuestion>();
        questionnaireQuestionRepository.findAll(questionnaireQuestionPredicate).forEach(questionList::add);
        Question question = questionRepository.getById(questionList.get(0).getQuestion().getId());
        QuestionDto questionDto = questionMapper.mapToQuestionDto(question);
        questionDto.setQuestionNumber(questionNumber);
        BooleanBuilder candidateAnswerPredicate = buildCandidateAnswerPredicate(userQuestionnaireId, question.getId());
        List<CandidateAnswer> answers = new ArrayList<CandidateAnswer>();
        candidateAnswerRepository.findAll(candidateAnswerPredicate).forEach(answers::add);
        BooleanBuilder answerPredicate = buildAnswerPredicate(question.getId());
        List<Answer> answersList = new ArrayList<Answer>();
        if (question.getType().equals(QuestionTypeEnum.OPEN_ENDED) && answers.size() != 0)
            questionDto.setAnswerText(answers.get(0).getTextAnswer());
        else if (question.getType().equals(QuestionTypeEnum.MULTIPLE_CHOICE)) {
            if (answers.size() != 0) questionDto.setAnswerText(answers.get(0).getAnswer().getId().toString());
            answerRepository.findAll(answerPredicate).forEach(answersList::add);
            List<PossibleAnswerDto> possibleAnswers = answersList.stream().map(answer -> answerMapper.mapToPossibleAnswerDto(answer)).collect(Collectors.toList());
            questionDto.setPossibleAnswers(possibleAnswers);
        } else if (question.getType().equals(QuestionTypeEnum.MULTIPLE_ANSWERS)) {
            answerRepository.findAll(answerPredicate).forEach(answersList::add);
            List<MultipleAnswerDto> multipleAnswerDtos = new ArrayList<>();
            answersList.stream().forEach(answer -> {
                MultipleAnswerDto multipleAnswerDto = answerMapper.mapToMultipleAnswerDto(answer);
                multipleAnswerDto.setValue(false);
                multipleAnswerDtos.add(multipleAnswerDto);
            });
            if (answers.size() != 0) {
                for (CandidateAnswer answer : answers) {
                    multipleAnswerDtos.stream().filter(x -> x.getId().equals(answer.getAnswer().getId())).forEach(x -> x.setValue(true));
                }
            }
            questionDto.setMultipleAnswer(multipleAnswerDtos);
        }
        return questionDto;
    }

    @Override
    public Page<ViewQuestionDto> getQuestions(Sort sort, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        BooleanBuilder questionPredicate = buildQuestionPredicate();
        Page<Question> questions = questionRepository.findAll(questionPredicate, pageRequest);
        return questions.map(question -> {
            ViewQuestionDto viewQuestionDto = questionMapper.mapToViewQuestionDto(question);
            if (question.getType().equals(QuestionTypeEnum.MULTIPLE_CHOICE) || question.getType().equals(QuestionTypeEnum.MULTIPLE_ANSWERS)) {
                BooleanBuilder answerPredicate = buildAnswerPredicate(question.getId());
                List<Answer> answersList = new ArrayList<Answer>();
                answerRepository.findAll(answerPredicate).forEach(answersList::add);
                List<AnswerDto> answers = answersList.stream().map(answer -> {
                    AnswerDto answerDto = answerMapper.mapToAnswerDto(answer);
                    return answerDto;
                }).collect(Collectors.toList());
                viewQuestionDto.setAnswers(answers);
            }
            return viewQuestionDto;
        });
    }


    private BooleanBuilder buildQuestionPredicate() {
        var qQuestion = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qQuestion.questionStatus.eq(QuestionStatusEnum.VALID));
        return booleanBuilder;
    }

    @Override
    public Page<ViewQuestionDto> findQuestionByContent(String questionContent, UUID selectedSkillId, SkillLevelEnum selectedSkillLevel, QuestionTypeEnum selectedQuestionType, Sort sort, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        BooleanBuilder predicate = buildSearchPredicate(questionContent, selectedSkillId, selectedSkillLevel, selectedQuestionType);
        Page<Question> questions = questionRepository.findAll(predicate, pageRequest);
        System.out.println(selectedQuestionType);
        return questions.map(question -> {
            ViewQuestionDto viewQuestionDto = questionMapper.mapToViewQuestionDto(question);
            if (question.getType().equals(QuestionTypeEnum.MULTIPLE_CHOICE) || question.getType().equals(QuestionTypeEnum.MULTIPLE_ANSWERS)) {
                BooleanBuilder answerPredicate = buildAnswerPredicate(question.getId());
                List<Answer> answersList = new ArrayList<Answer>();
                answerRepository.findAll(answerPredicate).forEach(answersList::add);
                List<AnswerDto> answers = answersList.stream().map(answerMapper::mapToAnswerDto).collect(Collectors.toList());
                viewQuestionDto.setAnswers(answers);
            }
            return viewQuestionDto;
        });
    }

    private BooleanBuilder buildSearchPredicate(String questionContent, UUID selectedSkillId, SkillLevelEnum selectedSkillLevel, QuestionTypeEnum selectedQuestionType) {
        var qQuestion = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Objects.nonNull(questionContent)) {
            booleanBuilder.and(qQuestion.questionEnglishFiltered.containsIgnoreCase(questionContent));
        }
        if (Objects.nonNull(selectedSkillId)) {
            booleanBuilder.and(qQuestion.skill.id.eq(selectedSkillId));
        }
        if (Objects.nonNull(selectedSkillLevel)) {
            booleanBuilder.and(qQuestion.skillLevel.eq(selectedSkillLevel));
        }
        if (Objects.nonNull(selectedQuestionType)) {
            booleanBuilder.and(qQuestion.type.eq(selectedQuestionType));
        }
        return booleanBuilder;
    }

    @Override
    public void deleteQuestion(UUID questionId) {
        BooleanBuilder answerPredicate = buildAnswerPredicate(questionId);
        List<Answer> answersList = new ArrayList<Answer>();
        answerRepository.findAll(answerPredicate).forEach(answersList::add);
        answerRepository.deleteAll(answersList);
        questionRepository.deleteById(questionId);
    }

    private BooleanBuilder buildQuestionSkillPredicate(UUID skillId) {
        var qQuestion = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qQuestion.skill.id.eq(skillId));
        return booleanBuilder;
    }

    @Override
    public List<DashboardDto> getDashboardData() {
        List<Skill> skills = skillRepository.findAll();
        List<DashboardDto> dashboardDtos = new ArrayList<>();
        skills.forEach(skill -> {
            BooleanBuilder questionPredicate = buildQuestionSkillPredicate(skill.getId());
            List<Question> questions = new ArrayList<Question>();
            questionRepository.findAll(questionPredicate).forEach(questions::add);
            int numberOfOpenEndedQuestions = 0;
            int numberOfMultipleChoiceQuestions = 0;
            int numberOfMultipleAnswersQuestions = 0;
            for (Question question : questions) {
                if (question.getType().equals(QuestionTypeEnum.OPEN_ENDED)) {
                    numberOfOpenEndedQuestions++;
                } else if (question.getType().equals(QuestionTypeEnum.MULTIPLE_CHOICE)) {
                    numberOfMultipleChoiceQuestions++;
                } else if (question.getType().equals(QuestionTypeEnum.MULTIPLE_ANSWERS)) {
                    numberOfMultipleAnswersQuestions++;
                }
            }
            DashboardDto dashboardDto = skillMapper.mapToDashboardDto(skill);
            dashboardDto.setNumberOfOpenEndedQuestions(numberOfOpenEndedQuestions);
            dashboardDto.setNumberOfMultipleChoiceQuestions(numberOfMultipleChoiceQuestions);
            dashboardDto.setNumberOfMultipleAnswersQuestions(numberOfMultipleAnswersQuestions);
            dashboardDtos.add(dashboardDto);
        });
        return dashboardDtos;
    }
}