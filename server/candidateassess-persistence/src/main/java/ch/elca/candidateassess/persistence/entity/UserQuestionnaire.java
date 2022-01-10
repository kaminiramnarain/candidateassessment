package ch.elca.candidateassess.persistence.entity;

import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_questionnaire")
public class UserQuestionnaire extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_questionnaire_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "marks")
    private Double marks;

    @Column(name = "time_taken")
    private Integer timeTakenToCompleteQuestionnaire;

    @Column(name = "remaining_time", nullable = false)
    private Integer remainingTime;

    @Column(name = "token")
    private String token;

    @Column(name = "candidate_select_skills")
    private Boolean candidateSelectSkills;

    @Column(name = "is_attempted")
    private Boolean isAttempted = false;

    @Column(name = "user_archived")
    private Boolean userArchived = false;

    @Column(name = "auto_generate")
    private Boolean autoGenerate;

    @Column(name = "cheat_count")
    private Integer cheatCount = 0;

    @Column(name = "interview_date")
    private LocalDateTime interviewDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionnaireStatusEnum status;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private Questionnaire questionnaire;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

}