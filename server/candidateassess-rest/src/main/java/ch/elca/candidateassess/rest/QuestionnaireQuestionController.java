package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.CreateQuestionnaireQuestionDto;
import ch.elca.candidateassess.dto.QuestionnaireDataForReviewDto;
import ch.elca.candidateassess.service.QuestionnaireQuestionService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/questionnaire-questions")
public class QuestionnaireQuestionController {

    private final QuestionnaireQuestionService questionnaireQuestionService;

    public QuestionnaireQuestionController(QuestionnaireQuestionService questionnaireQuestionService) {
        this.questionnaireQuestionService = questionnaireQuestionService;
    }

    @PostMapping
    public void createCandidateAnswer(@RequestBody CreateQuestionnaireQuestionDto createQuestionnaireQuestionDto) {
        questionnaireQuestionService.saveQuestionnaireQuestion(createQuestionnaireQuestionDto);
    }

    @GetMapping("/getDataForReview/{userQuestionnaireId}")
    public List<QuestionnaireDataForReviewDto> getQuestionsForReview(@PathVariable("userQuestionnaireId") String userQuestionnaireId) {
        return questionnaireQuestionService.getQuestionsForReview(userQuestionnaireId);
    }


}