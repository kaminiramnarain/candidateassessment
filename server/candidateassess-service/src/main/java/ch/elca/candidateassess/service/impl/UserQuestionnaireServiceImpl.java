package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.exception.ResourceNotFoundException;
import ch.elca.candidateassess.mapper.PositionMapper;
import ch.elca.candidateassess.mapper.UUIDMapper;
import ch.elca.candidateassess.mapper.UserQuestionnaireMapper;
import ch.elca.candidateassess.persistence.entity.*;
import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import ch.elca.candidateassess.persistence.repository.*;
import ch.elca.candidateassess.service.EmailSenderService;
import ch.elca.candidateassess.service.UserQuestionnaireService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserQuestionnaireServiceImpl implements UserQuestionnaireService {

    private final UserQuestionnaireRepository userQuestionnaireRepository;
    private final UserQuestionnaireMapper userQuestionnaireMapper;
    private final PositionRepository positionRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final CandidateAnswerRepository candidateAnswerRepository;
    private final PersonRepository personRepository;
    private final UUIDMapper uuidMapper;
    private final PositionMapper positionMapper;
    private final EmailSenderService emailSenderService;
    private final QuestionnaireQuestionRepository questionnaireQuestionRepository;

    public UserQuestionnaireServiceImpl(UserQuestionnaireRepository userQuestionnaireRepository, UserQuestionnaireMapper userQuestionnaireMapper, PositionRepository positionRepository, QuestionnaireRepository questionnaireRepository, CandidateAnswerRepository candidateAnswerRepository, PersonRepository personRepository, UUIDMapper uuidMapper, PositionMapper positionMapper, EmailSenderService emailSenderService, QuestionnaireQuestionRepository questionnaireQuestionRepository) {
        this.userQuestionnaireRepository = userQuestionnaireRepository;
        this.userQuestionnaireMapper = userQuestionnaireMapper;
        this.positionRepository = positionRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.candidateAnswerRepository = candidateAnswerRepository;
        this.personRepository = personRepository;
        this.uuidMapper = uuidMapper;
        this.positionMapper = positionMapper;
        this.emailSenderService = emailSenderService;
        this.questionnaireQuestionRepository = questionnaireQuestionRepository;
    }

    @Override
    public UserQuestionnaireDto saveUserQuestionnaire(CreateUserQuestionnaireDto createUserQuestionnaireDto) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireMapper.mapToUserQuestionnaire(createUserQuestionnaireDto);
        positionRepository.findById(createUserQuestionnaireDto.getPositionId()).ifPresentOrElse(userQuestionnaire::setPosition, () -> {
            throw new ResourceNotFoundException("Position does not exist!");
        });
        if (createUserQuestionnaireDto.getQuestionnaireId() != null) {
            questionnaireRepository.findById(createUserQuestionnaireDto.getQuestionnaireId()).ifPresentOrElse(userQuestionnaire::setQuestionnaire, () -> {
                throw new ResourceNotFoundException("Questionnaire does not exist!");
            });
        }
        userQuestionnaireRepository.save(userQuestionnaire);
        return userQuestionnaireMapper.mapToUserQuestionnaireDto(userQuestionnaire);
    }

    @Override
    public void updateUserQuestionnaire(UpdateUserQuestionnaireDto updateUserQuestionnaireDto, String userQuestionnaireId) {
        userQuestionnaireRepository.findById(uuidMapper.mapToUUID(userQuestionnaireId)).ifPresentOrElse(userQuestionnaire ->
        {
            userQuestionnaireMapper.mapUpdateUserQuestionnaireDtoToExistingUserQuestionnaire(userQuestionnaire, updateUserQuestionnaireDto);
            positionRepository.findById(updateUserQuestionnaireDto.getPositionId()).ifPresentOrElse(userQuestionnaire::setPosition, () -> {
                throw new ResourceNotFoundException("Position does not exist!");
            });
            userQuestionnaireRepository.save(userQuestionnaire);
        }, () -> {
            throw new ResourceNotFoundException("User Questionnaire not found!");
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

    public void updateRemainingTime(UpdateTimeDto updateTimeDto) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(updateTimeDto.getUserQuestionnaireId());
        userQuestionnaire.setRemainingTime(updateTimeDto.getRemainingTime());
        userQuestionnaireRepository.save(userQuestionnaire);
    }

    private BooleanBuilder buildValidateTokenPredicate(String token) {
        var qUserQuestionnaire = QUserQuestionnaire.userQuestionnaire;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qUserQuestionnaire.token.eq(token));
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
    public ValidateTokenDto validateToken(String token) {
        ValidateTokenDto validateTokenDto = new ValidateTokenDto();
        BooleanBuilder validateTokenPredicate = buildValidateTokenPredicate(token);
        List<UserQuestionnaire> userQuestionnaires = new ArrayList<UserQuestionnaire>();
        userQuestionnaireRepository.findAll(validateTokenPredicate).forEach(userQuestionnaires::add);
        if (userQuestionnaires.isEmpty()) {
            throw new ResourceNotFoundException("Incorrect token!");
        } else {
            UserQuestionnaire validatedUserQuestionnaire = userQuestionnaires.get(0);
            userQuestionnaireMapper.mapToValidateToken(validateTokenDto,validatedUserQuestionnaire);
            validatedUserQuestionnaire.setIsAttempted(true);
            userQuestionnaireRepository.save(validatedUserQuestionnaire);
        }
        return validateTokenDto;
    }

    @Override
    public void validateId(UUID userQuestionnaireId) {
        userQuestionnaireRepository.findById(userQuestionnaireId).ifPresentOrElse(userQuestionnaire -> {
        }, () -> {
            throw new ResourceNotFoundException("User Questionnaire does not exist!");
        });
    }

    @Override
    public void validateIdAndUnderReview(UUID userQuestionnaireId) {
        userQuestionnaireRepository.findById(userQuestionnaireId).stream()
                .filter(userQuestionnaire -> userQuestionnaire.getStatus().equals(QuestionnaireStatusEnum.UNDER_REVIEW))
                .findFirst()
                .ifPresentOrElse(userQuestionnaire -> {
                }, () -> {
                    throw new ResourceNotFoundException("User Questionnaire does not exist!");
                });
    }

    @Override
    public void inviteCandidate(InviteCandidateDto inviteCandidateDto) {

        userQuestionnaireRepository.findById(inviteCandidateDto.getId()).ifPresentOrElse(userQuestionnaire ->
        {
            Date dateTime = inviteCandidateDto.getInterviewDate();
            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm a");
            String formattedDate = DateFor.format(dateTime);

            List<String> dateList = Arrays.asList(formattedDate.split("/"));
            List<String> yearList = Arrays.asList(dateList.get(2).split(" "));

            String day = dateList.get(0);
            String month = dateList.get(1);
            String year = yearList.get(0);
            String token = userQuestionnaire.getFirstName().substring(0, 1).toUpperCase(Locale.ROOT)
                    + userQuestionnaire.getLastName().substring(0, 1).toUpperCase(Locale.ROOT)
                    + day + month + year;
            String subject = "Interview date";
            String body = "Dear " + userQuestionnaire.getFirstName() + "," + "\n" + "\n" + "Please note that your interview is scheduled for " + formattedDate + ". Please keep this token " + token + " in mind when you attend the interview. The token can only be used once. Good luck" + "\n" + "\n" + "Kind regards," + "\n" + "ELCA Information Technology";
            userQuestionnaireMapper.mapInviteCandidateDtoToExistingUserQuestionnaire(userQuestionnaire, inviteCandidateDto);
            userQuestionnaire.setToken(token);
            userQuestionnaireRepository.save(userQuestionnaire);
            emailSenderService.sendEmailToCandidate(userQuestionnaire.getEmailAddress(), subject, body);
        }, () -> {
            throw new ResourceNotFoundException("User Questionnaire not found!");
        });
    }

    @Override
    public void saveReviewedUserQuestionnaire(SaveReviewedUserQuestionnaireDto saveReviewedUserQuestionnaireDto, UUID userQuestionnaireId) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(userQuestionnaireId);
        userQuestionnaireMapper.mapSaveReviewedUserQuestionnaireDtoToExistingUserQuestionnaire(userQuestionnaire, saveReviewedUserQuestionnaireDto);
        userQuestionnaire.setStatus(QuestionnaireStatusEnum.COMPLETED);
        userQuestionnaireRepository.save(userQuestionnaire);
        Double marks = questionnaireRepository.getById(userQuestionnaire.getQuestionnaire().getId()).getMarks();
        String subject = "Interview result";
        String body = "Dear " + userQuestionnaire.getFirstName() + "," + "\n" + "\n" + "Congratulations, you have obtained " + saveReviewedUserQuestionnaireDto.getMarks() + " on " +  marks +" as total marks for the interview test. HR will get back to you shortly to explain to you the next steps of your application." + "\n" + "\n" + "Kind regards," + "\n" + "ELCA Information Technology";
        emailSenderService.sendEmailToCandidate(userQuestionnaire.getEmailAddress(), subject, body);
    }

    private BooleanBuilder buildCandidateAnswerPredicate(UUID userQuestionnaireId) {
        var qCandidateAnswer = QCandidateAnswer.candidateAnswer;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qCandidateAnswer.userQuestionnaire.id.eq(userQuestionnaireId));
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

    @Override
    public Boolean getQuestionnaireType(UUID userQuestionnaireId) {
        return userQuestionnaireRepository.getById(userQuestionnaireId).getAutoGenerate();
    }
}

