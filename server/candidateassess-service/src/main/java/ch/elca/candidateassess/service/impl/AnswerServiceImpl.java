package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.AnswerDto;
import ch.elca.candidateassess.mapper.AnswerMapper;
import ch.elca.candidateassess.persistence.entity.Answer;
import ch.elca.candidateassess.persistence.repository.AnswerRepository;
import ch.elca.candidateassess.persistence.repository.QuestionRepository;
import ch.elca.candidateassess.service.AnswerService;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerMapper answerMapper;

    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.answerMapper = answerMapper;
    }

    @Override
    public void saveAnswer(AnswerDto answerDto) {
        Answer answer = answerMapper.mapToAnswer(answerDto);
        answer.setQuestion(questionRepository.getById(answerDto.getQuestionId()));
        answerRepository.save(answer);
    }
}