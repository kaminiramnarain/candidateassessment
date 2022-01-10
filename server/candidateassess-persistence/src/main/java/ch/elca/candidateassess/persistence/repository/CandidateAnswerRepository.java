package ch.elca.candidateassess.persistence.repository;

import ch.elca.candidateassess.persistence.entity.CandidateAnswer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateAnswerRepository extends AbstractRepository<CandidateAnswer> {

    List<CandidateAnswer> findByUserQuestionnaireIdAndQuestionId(UUID userQuestionnaireId, UUID questionId);

}