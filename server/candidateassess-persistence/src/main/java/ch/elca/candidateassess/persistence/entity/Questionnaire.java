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
@Table(name = "questionnaire")
public class Questionnaire extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "questionnaire_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "marks")
    private Double marks;

    @Column(name = "total_time")
    private Integer totalTime;

}