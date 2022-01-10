package ch.elca.candidateassess.persistence.repository;

import ch.elca.candidateassess.persistence.entity.Answer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnswerRepository extends AbstractRepository<Answer> {

    List<Answer> findByQuestionId(UUID questionId);

}