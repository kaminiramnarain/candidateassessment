package ch.elca.candidateassess.persistence.entity;

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
@Table(name = "candidate_answer")
public class CandidateAnswer extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "candidate_answer_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "text_answer", columnDefinition = "TEXT")
    private String textAnswer;

    @Column(name = "marks_allocated")
    private Double marksAllocated;

    @ManyToOne
    @JoinColumn(name = "user_questionnaire_id", nullable = false)
    private UserQuestionnaire userQuestionnaire;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id")  // nullable true here because answer could be empty
    private Answer answer;

}