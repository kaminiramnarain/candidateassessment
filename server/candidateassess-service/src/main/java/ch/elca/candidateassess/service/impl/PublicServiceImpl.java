package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.exception.ResourceNotFoundException;
import ch.elca.candidateassess.mapper.*;
import ch.elca.candidateassess.persistence.entity.*;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import ch.elca.candidateassess.persistence.repository.*;
import ch.elca.candidateassess.service.PublicService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PublicServiceImpl implements PublicService {

    private final UserQuestionnaireRepository userQuestionnaireRepository;
    private final UserQuestionnaireMapper userQuestionnaireMapper;
    private final QuestionnaireQuestionRepository questionnaireQuestionRepository;
    private final CandidateAnswerRepository candidateAnswerRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final UserQuestionnaireSkillRepository userQuestionnaireSkillRepository;
    private final UUIDMapper uuidMapper;
    private final UserQuestionnaireSkillMapper userQuestionnaireSkillMapper;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final CandidateAnswerMapper candidateAnswerMapper;

    public PublicServiceImpl(UserQuestionnaireRepository userQuestionnaireRepository, UserQuestionnaireMapper userQuestionnaireMapper, QuestionnaireQuestionRepository questionnaireQuestionRepository, CandidateAnswerRepository candidateAnswerRepository, QuestionnaireRepository questionnaireRepository, SkillRepository skillRepository, SkillMapper skillMapper, UserQuestionnaireSkillRepository userQuestionnaireSkillRepository, UUIDMapper uuidMapper,  UserQuestionnaireSkillMapper userQuestionnaireSkillMapper, QuestionRepository questionRepository, QuestionMapper questionMapper, AnswerRepository answerRepository, AnswerMapper answerMapper, CandidateAnswerMapper candidateAnswerMapper) {
        this.userQuestionnaireRepository = userQuestionnaireRepository;
        this.questionnaireQuestionRepository = questionnaireQuestionRepository;
        this.userQuestionnaireMapper = userQuestionnaireMapper;
        this.candidateAnswerRepository=candidateAnswerRepository;
        this.questionnaireRepository=questionnaireRepository;
        this.skillRepository= skillRepository;
        this.skillMapper=skillMapper;
        this.userQuestionnaireSkillRepository=userQuestionnaireSkillRepository;
        this.uuidMapper=uuidMapper;
        this.userQuestionnaireSkillMapper=userQuestionnaireSkillMapper;
        this.questionRepository= questionRepository;
        this.questionMapper=questionMapper;
        this.answerRepository= answerRepository;
        this.answerMapper=answerMapper;
        this.candidateAnswerMapper=candidateAnswerMapper;
    }

    @Override
    public ValidateTokenDto validateToken(String token) {
        ValidateTokenDto validateTokenDto = new ValidateTokenDto();
        BooleanBuilder validateTokenPredicate = buildValidateTokenPredicate(token);
        List<UserQuestionnaire> userQuestionnaires = new ArrayList<UserQuestionnaire>();
        userQuestionnaireRepository.findAll(validateTokenPredicate).forEach(userQuestionnaires::add);
        if (userQuestionnaires.isEmpty()) {
            throw new ResourceNotFoundException("Incorrect token!");
        } else {
            UserQuestionnaire validatedUserQuestionnaire = userQuestionnaires.get(0);
            userQuestionnaireMapper.mapToValidateToken(validateTokenDto, validatedUserQuestionnaire);
            validatedUserQuestionnaire.setIsAttempted(true);
            userQuestionnaireRepository.save(validatedUserQuestionnaire);
        }
        return validateTokenDto;
    }

    private BooleanBuilder buildValidateTokenPredicate(String token) {
        var qUserQuestionnaire = QUserQuestionnaire.userQuestionnaire;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qUserQuestionnaire.token.eq(token));
    }

    @Override
    public void validateId(UUID userQuestionnaireId) {
        userQuestionnaireRepository.findById(userQuestionnaireId).ifPresentOrElse(userQuestionnaire -> {
        }, () -> {
            throw new ResourceNotFoundException("User Questionnaire does not exist!");
        });
    }

    @Override
    public Double getMarksByUserQuestionnaireId(UUID userQuestionnaireId) {
        UUID questionnaireId = userQuestionnaireRepository.getById(userQuestionnaireId).getQuestionnaire().getId();
        QQuestionnaire qQuestionnaire = QQuestionnaire.questionnaire;
        PageRequest page = PageRequest.of(0, 100);
        Double totalMarks = questionnaireQuestionRepository.findAll(qQuestionnaire.id.eq(questionnaireId), page).getContent().stream().map(QuestionnaireQuestion::getQuestion).mapToDouble(Question::getMarks).sum();
        BooleanBuilder predicate = buildCandidateAnswerPredicate(userQuestionnaireId);
        List<CandidateAnswer> candidateAnswers = new ArrayList<CandidateAnswer>();
        candidateAnswerRepository.findAll(predicate).forEach(candidateAnswers::add);

        Double totalAllocatedMarks = candidateAnswers.stream().mapToDouble(CandidateAnswer::getMarksAllocated).sum();
        Double percentage = (totalAllocatedMarks / totalMarks) * 100;

        Questionnaire questionnaire = questionnaireRepository.getById(userQuestionnaireRepository.getById(userQuestionnaireId).getQuestionnaire().getId());
        questionnaire.setMarks(totalMarks);
        questionnaireRepository.save(questionnaire);

        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(userQuestionnaireId);
        userQuestionnaire.setMarks(totalAllocatedMarks);
        userQuestionnaireRepository.save(userQuestionnaire);

        return Math.round(percentage * 100.0) / 100.0;
    }

    private BooleanBuilder buildCandidateAnswerPredicate(UUID userQuestionnaireId) {
        var qCandidateAnswer = QCandidateAnswer.candidateAnswer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qCandidateAnswer.userQuestionnaire.id.eq(userQuestionnaireId));
    }

    @Override
    public Boolean getQuestionnaireType(UUID userQuestionnaireId) {
        return userQuestionnaireRepository.getById(userQuestionnaireId).getAutoGenerate();
    }

    @Override
    public List<SkillDto> searchSkills(String name) {
        BooleanBuilder searchPredicate = buildSearchPredicate(name);
        List<Skill> skillList = new ArrayList<>();
        skillRepository.findAll(searchPredicate).forEach(skillList::add);
        return skillList.stream().map(skillMapper::mapToSkillDto).collect(Collectors.toList());
    }

    private BooleanBuilder buildSearchPredicate(String name) {
        var qSkill = QSkill.skill;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Objects.nonNull(name)) {
            booleanBuilder.and(qSkill.name.toLowerCase().contains(name.toLowerCase(Locale.ROOT)));
        }
        return booleanBuilder;
    }

    @Override
    public void saveUserQuestionnaireSkill(CreateUserQuestionnaireSkillDto createUserQuestionnaireSkillDto) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(createUserQuestionnaireSkillDto.getUserQuestionnaireId());
        createUserQuestionnaireSkillDto.getSkills().forEach(skill -> {
            Skill userSelectedSkill = skillRepository.getById(skill.getId());
            UserQuestionnaireSkill userQuestionnaireSkill = new UserQuestionnaireSkill();
            userQuestionnaireSkill.setUserQuestionnaire(userQuestionnaire);
            userQuestionnaireSkill.setSkill(userSelectedSkill);
            SkillLevelEnum selectedSkillLevel = SkillLevelEnum.NOVICE;
            if (skill.getLevel().equals(SkillLevelEnum.BEGINNER)) selectedSkillLevel = SkillLevelEnum.BEGINNER;
            else if (skill.getLevel().equals(SkillLevelEnum.PROFICIENT)) selectedSkillLevel = SkillLevelEnum.PROFICIENT;
            else if (skill.getLevel().equals(SkillLevelEnum.ADVANCED)) selectedSkillLevel = SkillLevelEnum.ADVANCED;
            else if (skill.getLevel().equals(SkillLevelEnum.EXPERT)) selectedSkillLevel = SkillLevelEnum.EXPERT;
            userQuestionnaireSkill.setLevel(selectedSkillLevel);
            userQuestionnaireSkillRepository.save(userQuestionnaireSkill);
        });
    }

    @Override
    public ValidateTokenDto getCandidateSelectSkillsStatus(String userQuestionnaireId) {
        ValidateTokenDto validateTokenDto = new ValidateTokenDto();
        userQuestionnaireRepository.findById(uuidMapper.mapToUUID(userQuestionnaireId)).ifPresentOrElse(userQuestionnaire -> {
            userQuestionnaireMapper.mapToValidateToken(validateTokenDto, userQuestionnaire);
        }, () -> {
            throw new ResourceNotFoundException("User Questionnaire not found!");
        });
        return validateTokenDto;
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

    private BooleanBuilder buildCandidateAnswerPredicate(UUID questionId, UUID userQuestionnaireId) {
        var qCandidateAnswer = QCandidateAnswer.candidateAnswer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qCandidateAnswer.userQuestionnaire.id.eq(userQuestionnaireId)).and(qCandidateAnswer.question.id.eq(questionId));
        return booleanBuilder;
    }


    private BooleanBuilder buildAnswerPredicate(UUID questionId) {
        var qAnswer = QAnswer.answer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qAnswer.question.id.eq(questionId));
    }

    private BooleanBuilder buildQuestionnaireQuestionPredicate(Integer questionNumber, UUID questionnaireId) {
        var qQuestionnaireQuestion = QQuestionnaireQuestion.questionnaireQuestion;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qQuestionnaireQuestion.questionnaire.id.eq(questionnaireId)).and(qQuestionnaireQuestion.questionNumber.eq(questionNumber));
        return booleanBuilder;
    }

    @Override
    public void updateCheatCount(UUID userQuestionnaireId) {
        UserQuestionnaire userQuestionnaire= userQuestionnaireRepository.getById(userQuestionnaireId);
        userQuestionnaire.setCheatCount(userQuestionnaire.getCheatCount()+1);
        userQuestionnaireRepository.save(userQuestionnaire);
    }

    @Override
    public Integer getCheatCount(UUID userQuestionnaireId) {
        UserQuestionnaire userQuestionnaire= userQuestionnaireRepository.getById(userQuestionnaireId);
        return userQuestionnaire.getCheatCount();
    }

    @Override
    public void disqualifyQuestionnaire(UUID userQuestionnaireId, Integer remainingTime) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(userQuestionnaireId);
        userQuestionnaire.setStatus(QuestionnaireStatusEnum.DISQUALIFIED);
        userQuestionnaire.setRemainingTime(remainingTime);
        Questionnaire questionnaire = questionnaireRepository.getById(userQuestionnaire.getQuestionnaire().getId());
        Integer timeTaken = questionnaire.getTotalTime() - remainingTime;
        userQuestionnaire.setTimeTakenToCompleteQuestionnaire(timeTaken);
        userQuestionnaireRepository.save(userQuestionnaire);
    }


    @Override
    public void updateRemainingTime(UpdateTimeDto updateTimeDto) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(updateTimeDto.getUserQuestionnaireId());
        userQuestionnaire.setRemainingTime(updateTimeDto.getRemainingTime());
        userQuestionnaireRepository.save(userQuestionnaire);
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

    @Override
    public void saveCandidateAnswer(CreateCandidateAnswerDto createCandidateAnswerDto) {
        CandidateAnswer candidateAnswer = candidateAnswerMapper.mapToCandidateAnswer(createCandidateAnswerDto);
        candidateAnswer.setUserQuestionnaire(userQuestionnaireRepository.getById(createCandidateAnswerDto.getUserQuestionnaireId()));
        candidateAnswer.setQuestion(questionRepository.getById(createCandidateAnswerDto.getQuestionId()));
        if (createCandidateAnswerDto.getQuestionType().equals(QuestionTypeEnum.OPEN_ENDED)) {
            List<CandidateAnswer> candidateAnswerList = candidateAnswerRepository.findByUserQuestionnaireIdAndQuestionId(createCandidateAnswerDto.getUserQuestionnaireId(), createCandidateAnswerDto.getQuestionId());
            if (candidateAnswerList.size() != 0) {
                for (var answer : candidateAnswerList) {
                    candidateAnswerRepository.deleteById(answer.getId());
                }
            }
            if (createCandidateAnswerDto.getTextAnswer() != null) {
                if (!createCandidateAnswerDto.getTextAnswer().equals("")) {
                    candidateAnswer.setTextAnswer(createCandidateAnswerDto.getTextAnswer());
                    candidateAnswer.setMarksAllocated(0.0);
                    candidateAnswerRepository.save(candidateAnswer);
                }
            }
        } else if (createCandidateAnswerDto.getQuestionType().equals(QuestionTypeEnum.MULTIPLE_CHOICE)) {
            List<CandidateAnswer> candidateAnswerList = candidateAnswerRepository.findAll().stream().filter(x -> x.getUserQuestionnaire().getId().equals(createCandidateAnswerDto.getUserQuestionnaireId()) && x.getQuestion().getId().equals(createCandidateAnswerDto.getQuestionId())).collect(Collectors.toList());
            if (candidateAnswerList.size() != 0) {
                for (var answer : candidateAnswerList) {
                    candidateAnswerRepository.deleteById(answer.getId());
                }
            }
            if (createCandidateAnswerDto.getTextAnswer() != null) {
                if (!createCandidateAnswerDto.getTextAnswer().equals("")) {
                    candidateAnswer.setAnswer(answerRepository.getById(UUID.fromString(createCandidateAnswerDto.getTextAnswer())));
                    boolean isAnswerValid = answerRepository.getById(candidateAnswer.getAnswer().getId()).getValid();
                    Double marks = questionRepository.getById(createCandidateAnswerDto.getQuestionId()).getMarks();
                    candidateAnswer.setMarksAllocated(isAnswerValid ? marks : 0.0);
                    candidateAnswerRepository.save(candidateAnswer);
                }
            }
        } else if (createCandidateAnswerDto.getQuestionType().equals(QuestionTypeEnum.MULTIPLE_ANSWERS)) {
            System.out.println(createCandidateAnswerDto.getMultipleAnswers());
            List<CandidateAnswer> candidateAnswerList = candidateAnswerRepository.findAll().stream().filter(x -> x.getUserQuestionnaire().getId().equals(createCandidateAnswerDto.getUserQuestionnaireId()) && x.getQuestion().getId().equals(createCandidateAnswerDto.getQuestionId())).collect(Collectors.toList());
            if (candidateAnswerList.size() != 0) {
                for (var answer : candidateAnswerList) {
                    candidateAnswerRepository.deleteById(answer.getId());
                }
            }

            List<Answer> answers = answerRepository.findByQuestionId(createCandidateAnswerDto.getQuestionId());
            int numberOfGoodAnswers = 0;
            int numberOfTrueSubmittedAnswers = 0;
            boolean valid = true;
            for (var answer : answers) {
                if (answer.getValid()) {
                    numberOfGoodAnswers++;
                }
            }
            for (var answer : createCandidateAnswerDto.getMultipleAnswers()) {
                if (answer.getValue()) {
                    numberOfTrueSubmittedAnswers++;
                }
            }

            if (numberOfTrueSubmittedAnswers != numberOfGoodAnswers) {
                valid = false;
            } else {

                List<MultipleAnswerDto> goodSubmittedAnswers = createCandidateAnswerDto.getMultipleAnswers().stream().filter(x -> x.getValue().equals(true)).collect(Collectors.toList());
                for (MultipleAnswerDto goodSubmittedAnswer : goodSubmittedAnswers) {
                    for (Answer answer : answers) {
                        if (goodSubmittedAnswer.getId().equals(answer.getId())) {
                            if (!goodSubmittedAnswer.getValue().equals(answer.getValid())) {
                                valid = false;
                                break;
                            }
                        }
                    }
                }
            }

            boolean finalValid = valid;
            int finalNumberOfGoodAnswers = numberOfGoodAnswers;

            System.out.println(valid);
            System.out.println(finalValid);

            createCandidateAnswerDto.getMultipleAnswers().forEach(answer -> {
                if (answer.getValue()) {
                    CandidateAnswer singleCandidateAnswer = new CandidateAnswer();
                    singleCandidateAnswer.setUserQuestionnaire(userQuestionnaireRepository.getById(createCandidateAnswerDto.getUserQuestionnaireId()));
                    singleCandidateAnswer.setQuestion(questionRepository.getById(createCandidateAnswerDto.getQuestionId()));
                    singleCandidateAnswer.setAnswer(answerRepository.getById(answer.getId()));
                    Double marks = questionRepository.getById(createCandidateAnswerDto.getQuestionId()).getMarks();
                    singleCandidateAnswer.setMarksAllocated(finalValid ? marks / finalNumberOfGoodAnswers : 0);
                    candidateAnswerRepository.save(singleCandidateAnswer);
                }
            });
        }
    }

    @Override
    public void finishQuestionnaire(UUID userQuestionnaireId, Integer remainingTime) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(userQuestionnaireId);
        userQuestionnaire.setStatus(QuestionnaireStatusEnum.UNDER_REVIEW);
        Questionnaire questionnaire = questionnaireRepository.getById(userQuestionnaire.getQuestionnaire().getId());
        Integer timeTaken = questionnaire.getTotalTime() - remainingTime;
        userQuestionnaire.setTimeTakenToCompleteQuestionnaire(timeTaken);
        userQuestionnaireRepository.save(userQuestionnaire);
    }


}
