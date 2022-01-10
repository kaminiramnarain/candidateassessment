package ch.elca.candidateassess.persistence.entity;

import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
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
@Table(name = "user_questionnaire_skill")
public class UserQuestionnaireSkill extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_questionnaire_skill_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_questionnaire_id", nullable = false)
    private UserQuestionnaire userQuestionnaire;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "skill_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private SkillLevelEnum level;

}