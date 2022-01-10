package ch.elca.candidateassess.persistence.entity;

import ch.elca.candidateassess.persistence.enumeration.QuestionStatusEnum;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "question")
public class Question extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "question_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "question_english", nullable = false, columnDefinition = "TEXT")
    private String questionEnglish;

    @Formula("REGEXP_REPLACE(question_english, '<[^>]*>', ' ','g')")
    private String questionEnglishFiltered;

    @Column(name = "question_french", columnDefinition = "TEXT")
    private String questionFrench;

    @Column(name = "question_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionStatusEnum questionStatus;

    @Column(name = "question_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionTypeEnum type;

    @Column(name = "marks", nullable = false)
    private Double marks;

    @Column(name = "time_assigned_for_question", nullable = false)
    private Integer timeAssignedForQuestion; // in seconds

    @Column(name = "skill_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private SkillLevelEnum skillLevel;

    @ManyToOne()
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

}