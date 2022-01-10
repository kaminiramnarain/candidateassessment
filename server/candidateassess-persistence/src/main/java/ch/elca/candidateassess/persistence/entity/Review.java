package ch.elca.candidateassess.persistence.entity;

import ch.elca.candidateassess.persistence.enumeration.ReviewStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review")
public class Review extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "review_id", updatable = false, nullable = false)
    private UUID id;

    //to implement user id by keycloak as foreign key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "user_questionnaire_id", nullable = false)
    private UserQuestionnaire userQuestionnaire;

}