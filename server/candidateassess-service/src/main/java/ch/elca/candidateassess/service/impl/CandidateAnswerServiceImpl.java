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
    public void saveCandidateMarks(SaveCandidateMarksDto candidateMarksDto) {
        if (candidateMarksDto.getCandidateAnswerId() != null) {
            CandidateAnswer candidateAnswer = candidateAnswerRepository.getById(candidateMarksDto.getCandidateAnswerId());
            candidateAnswer.setMarksAllocated(candidateMarksDto.getMarksAllocated());
            candidateAnswerRepository.save(candidateAnswer);
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