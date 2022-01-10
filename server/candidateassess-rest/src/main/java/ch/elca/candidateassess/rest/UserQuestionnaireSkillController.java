package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.CreateUserQuestionnaireSkillDto;
import ch.elca.candidateassess.service.UserQuestionnaireSkillService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/user-questionnaire-skills")
public class UserQuestionnaireSkillController {

    private final UserQuestionnaireSkillService userQuestionnaireSkillService;

    public UserQuestionnaireSkillController(UserQuestionnaireSkillService userQuestionnaireSkillService) {
        this.userQuestionnaireSkillService = userQuestionnaireSkillService;
    }

    @PostMapping
    public void createUserQuestionnaireSkill(@RequestBody CreateUserQuestionnaireSkillDto createUserQuestionnaireSkillDto) {
        userQuestionnaireSkillService.saveUserQuestionnaireSkill(createUserQuestionnaireSkillDto);
    }

}