package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.mapper.QuestionnaireMapper;
import ch.elca.candidateassess.mapper.UUIDMapper;
import ch.elca.candidateassess.mapper.*;
import ch.elca.candidateassess.persistence.entity.*;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import ch.elca.candidateassess.persistence.repository.*;
import ch.elca.candidateassess.service.QuestionnaireService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final UserQuestionnaireRepository userQuestionnaireRepository;
    private final UserQuestionnaireSkillRepository userQuestionnaireSkillRepository;
    private final QuestionRepository questionRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionnaireQuestionRepository questionnaireQuestionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionnaireMapper questionnaireMapper;
    private final AnswerMapper answerMapper;
    private final UserQuestionnaireMapper userQuestionnaireMapper;
    private final QuestionnaireQuestionMapper questionnaireQuestionMapper;
    private final UserQuestionnaireSkillMapper userQuestionnaireSkillMapper;
    private final CandidateAnswerRepository candidateAnswerRepository;
    private final UUIDMapper uuidMapper;

    public QuestionnaireServiceImpl(UserQuestionnaireRepository userQuestionnaireRepository, UserQuestionnaireMapper userQuestionnaireMapper, AnswerMapper answerMapper, QuestionnaireQuestionMapper questionnaireQuestionMapper, UserQuestionnaireSkillRepository userQuestionnaireSkillRepository, QuestionRepository questionRepository, QuestionnaireRepository questionnaireRepository, QuestionnaireQuestionRepository questionnaireQuestionRepository, AnswerRepository answerRepository, QuestionnaireMapper questionnaireMapper, UserQuestionnaireSkillMapper userQuestionnaireSkillMapper, CandidateAnswerRepository candidateAnswerRepository, UUIDMapper uuidMapper) {

        this.userQuestionnaireRepository = userQuestionnaireRepository;
        this.userQuestionnaireSkillRepository = userQuestionnaireSkillRepository;
        this.questionRepository = questionRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireQuestionRepository = questionnaireQuestionRepository;
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
        this.questionnaireQuestionMapper = questionnaireQuestionMapper;
        this.questionnaireMapper = questionnaireMapper;
        this.userQuestionnaireMapper = userQuestionnaireMapper;
        this.userQuestionnaireSkillMapper = userQuestionnaireSkillMapper;
        this.candidateAnswerRepository = candidateAnswerRepository;
        this.uuidMapper = uuidMapper;
    }

    @Override
    public QuestionnaireDto saveQuestionnaire(CreateQuestionnaireDto createQuestionnaireDto) {
        Questionnaire questionnaire = questionnaireMapper.mapToQuestionnaire(createQuestionnaireDto);
        questionnaireRepository.save(questionnaire);
        return questionnaireMapper.mapToQuestionnaireDto(questionnaire);
    }


    private BooleanBuilder buildQuestionnaireLengthPredicate(UUID userQuestionnaireId) {
        var qUserQuestionnaireSkill = QUserQuestionnaireSkill.userQuestionnaireSkill;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qUserQuestionnaireSkill.userQuestionnaire.id.eq(userQuestionnaireId));
    }

    private BooleanBuilder buildQuestionsPredicate(Skill skill, SkillLevelEnum level) {
        var qQuestion = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qQuestion.skill.eq(skill)).and(qQuestion.skillLevel.eq(level));
    }


    @Override
    public void generateQuestionnaire(UUID userQuestionnaireId) {

        if (!userQuestionnaireRepository.getById(userQuestionnaireId).getStatus().equals(QuestionnaireStatusEnum.QUESTIONNAIRE_NOT_GENERATED))
            return;
        Integer questionnaireLength = questionnaireRepository.getById(userQuestionnaireRepository.getById(userQuestionnaireId).getQuestionnaire().getId()).getTotalTime();
        BooleanBuilder predicate = buildQuestionnaireLengthPredicate(userQuestionnaireId);
        List<UserQuestionnaireSkill> userQuestionnaireSkills = new ArrayList<UserQuestionnaireSkill>();
        userQuestionnaireSkillRepository.findAll(predicate).forEach(userQuestionnaireSkills::add);
        Integer amountOfSkills = Math.toIntExact((long) (int) userQuestionnaireSkills.stream().count());
        UUID questionnaireId = userQuestionnaireRepository.getById(userQuestionnaireId).getQuestionnaire().getId();
        int timePerSkill = questionnaireLength / amountOfSkills;
        List<SkillLevelDto> skillList = userQuestionnaireSkills.stream().map(userQuestionnaireSkillMapper::mapToSkillLevelDto).collect(Collectors.toList());

        ArrayList<Question> selectedQuestions = new ArrayList<>();
        for (int i = 0; i < amountOfSkills; i++) {
            int totalTimePerSkill = 0;
            SkillLevelDto selectedSkillLevelDto = skillList.get(i);
            BooleanBuilder questionPredicate = buildQuestionsPredicate(selectedSkillLevelDto.getSkill(), selectedSkillLevelDto.getLevel());
            List<Question> tempQuestions = new ArrayList<Question>();
            questionRepository.findAll(questionPredicate).forEach(tempQuestions::add);
            do {
                Random random = new Random();

                Question tempQuestion = tempQuestions.get(random.nextInt(tempQuestions.size()));
                totalTimePerSkill += tempQuestion.getTimeAssignedForQuestion();
                selectedQuestions.add(tempQuestion);
                tempQuestions.remove(tempQuestion);
            } while (totalTimePerSkill < timePerSkill);
        }

        for (int i = 0; i < selectedQuestions.size(); i++) {
            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            questionnaireQuestion.setQuestionnaire(questionnaireRepository.getById(questionnaireId));
            questionnaireQuestion.setQuestion(selectedQuestions.get(i));
            questionnaireQuestion.setQuestionNumber(i + 1);
            questionnaireQuestionRepository.save(questionnaireQuestion);
        }

        userQuestionnaireRepository.findById(userQuestionnaireId).ifPresent(userQuestionnaire -> {
            userQuestionnaire.setStatus(QuestionnaireStatusEnum.PENDING);
            userQuestionnaireRepository.save(userQuestionnaire);
        });
    }

    private BooleanBuilder buildQuestionsCountPredicate(UUID questionnaireId) {
        var qQuestionnaireQuestion = QQuestionnaireQuestion.questionnaireQuestion;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qQuestionnaireQuestion.questionnaire.id.eq(questionnaireId));
    }

    private BooleanBuilder buildIdPredicate(UUID questionnaireId, Integer index) {
        var qQuestionnaireQuestion = QQuestionnaireQuestion.questionnaireQuestion;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qQuestionnaireQuestion.questionnaire.id.eq(questionnaireId)).and(qQuestionnaireQuestion.questionNumber.eq(index));
    }


    private BooleanBuilder buildCandidateAnswersPredicate(UUID questionId, UUID userQuestionnaireId) {
        var qCandidateAnswer = QCandidateAnswer.candidateAnswer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and((qCandidateAnswer.question.id.eq(questionId)).and(qCandidateAnswer.userQuestionnaire.id.eq(userQuestionnaireId)));
    }

    private BooleanBuilder buildCandidateAnswerPredicate(UUID questionId, UUID userQuestionnaireId, UUID answerId) {
        var qCandidateAnswer = QCandidateAnswer.candidateAnswer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qCandidateAnswer.question.id.eq(questionId)).and(qCandidateAnswer.userQuestionnaire.id.eq(userQuestionnaireId)).and(qCandidateAnswer.answer.id.eq(answerId));
    }

    private BooleanBuilder buildAnswerPredicate(UUID questionId) {
        var qAnswer = QAnswer.answer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and((qAnswer.question.id.eq(questionId)));
    }

    @Override
    public QuestionnaireDataDto getQuestionnaireData(UUID userQuestionnaireId) {

        QuestionnaireDataDto questionnaireDataDto = new QuestionnaireDataDto();
        questionnaireDataDto.setDuration(questionnaireRepository.getById(userQuestionnaireRepository.getById(userQuestionnaireId).getQuestionnaire().getId()).getTotalTime());
        BooleanBuilder predicate = buildQuestionsCountPredicate(userQuestionnaireRepository.getById(userQuestionnaireId).getQuestionnaire().getId());
        List<QuestionnaireQuestion> questionnaireQuestions = new ArrayList<QuestionnaireQuestion>();
        questionnaireQuestionRepository.findAll(predicate).forEach(questionnaireQuestions::add);
        var numberOfQuestions = questionnaireQuestions.stream().count();

        List<IsAnsweredQuestionDto> isAnsweredQuestionDtos = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++) {
            int index = i;
            int correctIndex = i + 1;
            BooleanBuilder idPredicate = buildIdPredicate(userQuestionnaireRepository.getById(userQuestionnaireId).getQuestionnaire().getId(), correctIndex);
            List<QuestionnaireQuestion> questionnaireQuestionList = new ArrayList<QuestionnaireQuestion>();
            questionnaireQuestionRepository.findAll(idPredicate).forEach(questionnaireQuestionList::add);
            UUID questionId = questionnaireQuestionList.get(0).getQuestion().getId();

//            BooleanBuilder answerPredicate = buildAnswerPredicate(questionId, userQuestionnaireId);
//            List<CandidateAnswer> answers = new ArrayList<CandidateAnswer>();
//            candidateAnswerRepository.findAll(answerPredicate).forEach(answers::add);
            //when implementing boolean here problem
            List<CandidateAnswer> answers = candidateAnswerRepository.findAll().stream().filter(x -> x.getQuestion().getId().equals(questionId) && x.getUserQuestionnaire().getId().equals(userQuestionnaireId)).collect(Collectors.toList());

            IsAnsweredQuestionDto isAnsweredQuestionDto = new IsAnsweredQuestionDto();
            isAnsweredQuestionDto.setAnswered(!answers.isEmpty());
            isAnsweredQuestionDto.setQuestionNumber(i + 1);
            isAnsweredQuestionDtos.add(isAnsweredQuestionDto);
        }
        questionnaireDataDto.setQuestions(isAnsweredQuestionDtos);
        questionnaireDataDto.setRemainingTime(userQuestionnaireRepository.getById(userQuestionnaireId).getRemainingTime());
        QuestionnaireStatusEnum questionnaireStatus = userQuestionnaireRepository.getById(userQuestionnaireId).getStatus();
        questionnaireDataDto.setQuestionnaireOpen(questionnaireStatus == QuestionnaireStatusEnum.PENDING);
        return questionnaireDataDto;
    }

    @Override
    public FilledQuestionnaireDto getQuestionnaire(UUID userQuestionnaireId) {
        FilledQuestionnaireDto filledQuestionnaire = new FilledQuestionnaireDto();
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(userQuestionnaireId);

        UUID questionnaireId = userQuestionnaireRepository.getById(userQuestionnaireId).getQuestionnaire().getId();

        BooleanBuilder predicate = buildQuestionsCountPredicate(questionnaireId);
        List<QuestionnaireQuestion> questionnaireQuestions = new ArrayList<QuestionnaireQuestion>();
        Sort sort = Sort.by(Sort.Direction.ASC, "questionNumber");
        questionnaireQuestionRepository.findAll(predicate, sort).forEach(questionnaireQuestions::add);

        List<FilledQuestionDto> filledQuestionDtos = questionnaireQuestions.stream().map(questionnaireQuestion -> {
                    FilledQuestionDto filledQuestionDto = questionnaireQuestionMapper.mapToFilledQuestionDto(questionnaireQuestion);
                    BooleanBuilder answerPredicate = buildCandidateAnswersPredicate(questionnaireQuestion.getQuestion().getId(), userQuestionnaireId);
                    List<CandidateAnswer> candidateAnswerList = new ArrayList<CandidateAnswer>();
                    candidateAnswerRepository.findAll(answerPredicate).forEach(candidateAnswerList::add);
                    Double marksAllocated = 0.0;
                    if (candidateAnswerList.size() > 0) {
                        for (var answer : candidateAnswerList) {
                            marksAllocated += answer.getMarksAllocated();
                        }
                    }
                    filledQuestionDto.setMarksObtained(candidateAnswerList.size() == 0 ? 0.0 : marksAllocated);
                    if (questionnaireQuestion.getQuestion().getType().equals(QuestionTypeEnum.OPEN_ENDED)) {
                        BooleanBuilder candidateAnswerPredicate = buildCandidateAnswersPredicate(questionnaireQuestion.getQuestion().getId(), userQuestionnaireId);
                        List<CandidateAnswer> candidateAnswers = new ArrayList<CandidateAnswer>();
                        candidateAnswerRepository.findAll(candidateAnswerPredicate).forEach(candidateAnswers::add);
                        if (candidateAnswers.size() > 0) {
                            filledQuestionDto.setAnswerText(candidateAnswers.get(0).getTextAnswer());
                        } else {
                            filledQuestionDto.setAnswerText("");
                        }
                    } else {
                        filledQuestionDto.setAnswerText("");
                    }
                    List<FilledAnswerDto> answerDtos = new ArrayList<>();
                    if (!questionnaireQuestion.getQuestion().getType().equals(QuestionTypeEnum.OPEN_ENDED)) {
                        BooleanBuilder answersPredicate = buildAnswerPredicate(questionnaireQuestion.getQuestion().getId());
                        List<Answer> answers = new ArrayList<Answer>();
                        answerRepository.findAll(answersPredicate).forEach(answers::add);
                        for (var answer : answers) {
                            FilledAnswerDto filledAnswerDto = answerMapper.mapToFilledAnswerDto(answer);
                            BooleanBuilder candidateAnswerPredicate = buildCandidateAnswerPredicate(questionnaireQuestion.getQuestion().getId(), userQuestionnaireId, answer.getId());
                            List<CandidateAnswer> candidateAnswers = new ArrayList<CandidateAnswer>();
                            candidateAnswerRepository.findAll(candidateAnswerPredicate).forEach(candidateAnswers::add);


                            filledAnswerDto.setSelected(candidateAnswers.size() > 0);
                            answerDtos.add(filledAnswerDto);
                        }
                    }
                    filledQuestionDto.setAnswers(answerDtos);

                    return filledQuestionDto;
                }
        ).collect(Collectors.toList());


        userQuestionnaireMapper.mapToFilledQuestionnaireDto(filledQuestionnaire, userQuestionnaire);
        filledQuestionnaire.setQuestions(filledQuestionDtos);
        filledQuestionnaire.setMarks(questionnaireRepository.getById(userQuestionnaire.getQuestionnaire().getId()).getMarks());
        filledQuestionnaire.setLength(questionnaireRepository.getById(userQuestionnaire.getQuestionnaire().getId()).getTotalTime());
        return filledQuestionnaire;


    }

    @Override
    public void customizeQuestionnaire(UUID userQuestionnaireId, List<String> idList) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(userQuestionnaireId);
        UUID questionnaireId = userQuestionnaire.getQuestionnaire().getId();
        Questionnaire questionnaire = questionnaireRepository.getById(questionnaireId);
        List<Question> questions = new ArrayList<Question>();
        idList.stream().forEach(questionId -> {
            Question question = questionRepository.getById(uuidMapper.mapToUUID(questionId));
            questions.add(question);
        });
        Double questionnaireMarks = questions.stream().mapToDouble(question -> question.getMarks()).sum();
        System.out.println(questionnaireMarks);
        Integer questionnaireTime = questions.stream().mapToInt(question -> question.getTimeAssignedForQuestion()).sum();
        System.out.println(questionnaireTime);
        questionnaire.setMarks(questionnaireMarks);
        questionnaireRepository.save(questionnaire);
        int length = questions.size();
        IntStream.range(0, length).forEach(index -> {
            QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
            questionnaireQuestion.setQuestion(questions.get(index));
            questionnaireQuestion.setQuestionnaire(questionnaire);
            questionnaireQuestion.setQuestionNumber(index + 1);
            questionnaireQuestionRepository.save(questionnaireQuestion);
        });
        userQuestionnaire.setStatus(QuestionnaireStatusEnum.PENDING);
        userQuestionnaireRepository.save(userQuestionnaire);
    }
}
