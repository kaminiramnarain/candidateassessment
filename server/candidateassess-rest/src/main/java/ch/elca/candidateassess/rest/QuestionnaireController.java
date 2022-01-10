package ch.elca.candidateassess.rest;


import ch.elca.candidateassess.dto.CreateQuestionnaireDto;
import ch.elca.candidateassess.dto.FilledQuestionnaireDto;
import ch.elca.candidateassess.dto.QuestionnaireDataDto;
import ch.elca.candidateassess.dto.QuestionnaireDto;
import ch.elca.candidateassess.service.QuestionnaireService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/questionnaires")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @PostMapping
    public QuestionnaireDto createQuestionnaire(@RequestBody @Valid CreateQuestionnaireDto createQuestionnaireDto) {
        return questionnaireService.saveQuestionnaire(createQuestionnaireDto);
    }

    @PostMapping("/customizeQuestionnaire/{userQuestionnaireId}")
    public void customizeQuestionnaire(@PathVariable("userQuestionnaireId") UUID userQuestionnaireId, @RequestBody List<String> idList) {
        System.out.println("here");
        this.questionnaireService.customizeQuestionnaire(userQuestionnaireId, idList);
    }

    @PostMapping("generate")
    public void generateQuestionnaire(@RequestParam UUID userQuestionnaireId) {
        this.questionnaireService.generateQuestionnaire(userQuestionnaireId);
    }

    @GetMapping("getData/{userQuestionnaireId}")
    public QuestionnaireDataDto getQuestionnaireData(@PathVariable UUID userQuestionnaireId) {
        return this.questionnaireService.getQuestionnaireData(userQuestionnaireId);
    }

    @GetMapping("getQuestionnaire/{userQuestionnaireId}")
    public FilledQuestionnaireDto getQuestionnaire(@PathVariable UUID userQuestionnaireId) {
        return this.questionnaireService.getQuestionnaire(userQuestionnaireId);
    }

}