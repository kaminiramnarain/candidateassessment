package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.service.UserQuestionnaireService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/user-questionnaires")
public class UserQuestionnaireController {

    private final UserQuestionnaireService userQuestionnaireService;

    public UserQuestionnaireController(UserQuestionnaireService userQuestionnaireService) {
        this.userQuestionnaireService = userQuestionnaireService;
    }

    @GetMapping("/validateToken/{token}")
    public ValidateTokenDto validateToken(@PathVariable String token) {
        return userQuestionnaireService.validateToken(token);
    }

    @GetMapping("/validateId/{userQuestionnaireId}")
    public void validateId(@PathVariable UUID userQuestionnaireId) {
         userQuestionnaireService.validateId(userQuestionnaireId);
    }

    @GetMapping("/validateIdAndUnderReview/{userQuestionnaireId}")
    public void validateIdAndUnderReview(@PathVariable UUID userQuestionnaireId) {
        userQuestionnaireService.validateIdAndUnderReview(userQuestionnaireId);
    }

    @GetMapping("/getCheatCount/{userQuestionnaireId}")
    public Integer getCheatCount(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId) {
        return userQuestionnaireService.getCheatCount(userQuestionnaireId);
    }

    @GetMapping("getCandidateSelectSkillsStatus/{userQuestionnaireId}")
    public ValidateTokenDto getCandidateSelectSkillsStatus(@PathVariable("userQuestionnaireId") String userQuestionnaireId) {
        return userQuestionnaireService.getCandidateSelectSkillsStatus(userQuestionnaireId);
    }

    @GetMapping("getQuestionnaireType/{userQuestionnaireId}")
    public Boolean getQuestionnaireType(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId) {
        return userQuestionnaireService.getQuestionnaireType(userQuestionnaireId);
    }

    @GetMapping("/marks/{userQuestionnaireId}")
    public Double getMarkByUserQuestionnaireId(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId) {
        return userQuestionnaireService.getMarksByUserQuestionnaireId(userQuestionnaireId);
    }

    @PutMapping("/saveReviewedUserQuestionnaire/{userQuestionnaireId}")
    public void saveReviewedUserQuestionnaire(@RequestBody SaveReviewedUserQuestionnaireDto saveReviewedUserQuestionnaireDto, @PathVariable("userQuestionnaireId") UUID userQuestionnaireId) {
        userQuestionnaireService.saveReviewedUserQuestionnaire(saveReviewedUserQuestionnaireDto, userQuestionnaireId);
    }

    @PutMapping("/inviteCandidate/{userQuestionnaireId}")
    public void inviteCandidate(@RequestBody InviteCandidateDto inviteCandidateDto, @PathVariable("userQuestionnaireId") String userQuestionnaireId) {
        userQuestionnaireService.inviteCandidate(inviteCandidateDto);
    }

    @PutMapping("/disqualifyQuestionnaire/{userQuestionnaireId}")
    public void disqualifyQuestionnaire(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId, @RequestParam Integer remainingTime) {
        userQuestionnaireService.disqualifyQuestionnaire(userQuestionnaireId, remainingTime);
    }

    @PutMapping("/update/{userQuestionnaireId}")
    public void createUserQuestionnaire(@RequestBody UpdateUserQuestionnaireDto updateUserQuestionnaireDto, @PathVariable("userQuestionnaireId") String userQuestionnaireId) {
        userQuestionnaireService.updateUserQuestionnaire(updateUserQuestionnaireDto, userQuestionnaireId);
    }

    @PutMapping("/updateRemainingTime")
    public void updateRemainingTime(@RequestBody UpdateTimeDto updateTimeDto) {
        userQuestionnaireService.updateRemainingTime(updateTimeDto);
    }

    @PutMapping("/updateCheatCount/{userQuestionnaireId}")
    public void updateCheatCount(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId) {
        userQuestionnaireService.updateCheatCount(userQuestionnaireId);
    }

    @PostMapping
    public UserQuestionnaireDto createUserQuestionnaire(@RequestBody CreateUserQuestionnaireDto createUserQuestionnaireDto) {
        return userQuestionnaireService.saveUserQuestionnaire(createUserQuestionnaireDto);
    }

}