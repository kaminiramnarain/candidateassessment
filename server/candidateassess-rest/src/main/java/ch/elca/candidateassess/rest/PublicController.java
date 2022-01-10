package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.service.PublicService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/public")
public class PublicController {

    private final PublicService publicService;

    public PublicController(PublicService publicService) {
        this.publicService = publicService;
    }

    @GetMapping("/validateToken/{token}")
    public ValidateTokenDto validateToken(@PathVariable String token) {
        return publicService.validateToken(token);
    }

    @GetMapping("/validateId/{userQuestionnaireId}")
    public void validateId(@PathVariable UUID userQuestionnaireId) {
        publicService.validateId(userQuestionnaireId);
    }

    @GetMapping("/marks/{userQuestionnaireId}")
    public Double getMarkByUserQuestionnaireId(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId) {
        return publicService.getMarksByUserQuestionnaireId(userQuestionnaireId);
    }

    @GetMapping("getQuestionnaireType/{userQuestionnaireId}")
    public Boolean getQuestionnaireType(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId) {
        return publicService.getQuestionnaireType(userQuestionnaireId);
    }

    @GetMapping("/skills")
    public List<SkillDto> searchSkills(@RequestParam(required = false) String skillName) {
        return publicService.searchSkills(skillName);
    }

    @PostMapping("/user-questionnaire-skills")
    public void createUserQuestionnaireSkill(@RequestBody CreateUserQuestionnaireSkillDto createUserQuestionnaireSkillDto) {
        publicService.saveUserQuestionnaireSkill(createUserQuestionnaireSkillDto);
    }

    @GetMapping("getCandidateSelectSkillsStatus/{userQuestionnaireId}")
    public ValidateTokenDto getCandidateSelectSkillsStatus(@PathVariable("userQuestionnaireId") String userQuestionnaireId) {
        return publicService.getCandidateSelectSkillsStatus(userQuestionnaireId);
    }

    @PostMapping("/generate")
    public void generateQuestionnaire(@RequestParam UUID userQuestionnaireId) {
        this.publicService.generateQuestionnaire(userQuestionnaireId);
    }

    @GetMapping("/question")
    public QuestionDto getQuestion(@RequestParam UUID userQuestionnaireId, @RequestParam Integer questionNumber) {
        return this.publicService.getQuestion(userQuestionnaireId, questionNumber);
    }

    @GetMapping("/getCheatCount/{userQuestionnaireId}")
    public Integer getCheatCount(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId) {
        return publicService.getCheatCount(userQuestionnaireId);
    }

    @PutMapping("/updateCheatCount/{userQuestionnaireId}")
    public void updateCheatCount(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId) {
        publicService.updateCheatCount(userQuestionnaireId);
    }

    @PutMapping("/disqualifyQuestionnaire/{userQuestionnaireId}")
    public void disqualifyQuestionnaire(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId, @RequestParam Integer remainingTime) {
        publicService.disqualifyQuestionnaire(userQuestionnaireId, remainingTime);
    }

    @PutMapping("/updateRemainingTime")
    public void updateRemainingTime(@RequestBody UpdateTimeDto updateTimeDto) {
        publicService.updateRemainingTime(updateTimeDto);
    }

    @GetMapping("getData/{userQuestionnaireId}")
    public QuestionnaireDataDto getQuestionnaireData(@PathVariable UUID userQuestionnaireId) {
        return publicService.getQuestionnaireData(userQuestionnaireId);
    }

    @PostMapping("/saveAnswer")
    public void createCandidateAnswer(@RequestBody CreateCandidateAnswerDto createCandidateAnswerDto) {
        publicService.saveCandidateAnswer(createCandidateAnswerDto);
    }

    @PutMapping("/finish")
    public void finishQuestionnaire(@RequestParam UUID userQuestionnaireId, @RequestParam Integer remainingTime) {
        publicService.finishQuestionnaire(userQuestionnaireId, remainingTime);
    }







}

