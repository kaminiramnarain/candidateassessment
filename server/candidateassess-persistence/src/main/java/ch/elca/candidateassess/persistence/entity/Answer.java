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
@Table(name = "answer")
public class Answer extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "answer_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "answer_english", nullable = false, columnDefinition = "TEXT")
    private String answerEnglish;

    @Column(name = "answer_french", columnDefinition = "TEXT")
    private String answerFrench;

    @Column(name = "valid", nullable = false)
    private Boolean valid;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

}