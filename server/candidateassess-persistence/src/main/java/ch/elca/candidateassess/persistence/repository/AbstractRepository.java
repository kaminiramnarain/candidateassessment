package ch.elca.candidateassess.persistence.repository;

import ch.elca.candidateassess.persistence.entity.AuditModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * @author akn
 */
@NoRepositoryBean
public interface AbstractRepository<T extends AuditModel> extends JpaRepository<T, UUID>, QuerydslPredicateExecutor<T> {

}