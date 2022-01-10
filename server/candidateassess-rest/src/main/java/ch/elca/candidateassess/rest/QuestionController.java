package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.persistence.enumeration.QuestionTypeEnum;
import ch.elca.candidateassess.persistence.enumeration.SkillLevelEnum;
import ch.elca.candidateassess.dto.CreateQuestionDto;
import ch.elca.candidateassess.dto.QuestionDto;
import ch.elca.candidateassess.dto.SaveQuestionDto;
import ch.elca.candidateassess.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping()
    public QuestionDto getQuestion(@RequestParam UUID userQuestionnaireId, @RequestParam Integer questionNumber) {
        return this.questionService.getQuestion(userQuestionnaireId, questionNumber);
    }

    @GetMapping("/validateId/{questionId}")
    public void validateId(@PathVariable UUID questionId) {
        questionService.validateId(questionId);
    }


    @GetMapping("/all")
    public Page<ViewQuestionDto> getQuestions(@RequestParam(value = "sortOrder") String sortOrder,
                                              @RequestParam(value = "sortBy") String sortBy,
                                              @RequestParam(value = "pageNumber") Integer pageNumber,
                                              @RequestParam(value = "pageSize") Integer pageSize) {
        Sort sort = Sort.by("DESC".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return questionService.getQuestions(sort, pageNumber, pageSize);
    }

    @GetMapping("/{questionId}")
    public EditQuestionDto getQuestionById(@PathVariable("questionId") UUID questionId) {
        return questionService.getQuestionById(questionId);
    }

    @GetMapping("/search")
    public Page<ViewQuestionDto> findQuestionByContent(@RequestParam(value = "questionContent", required = false) String questionContent,
                                                   @RequestParam(value= "selectedSkillId", required = false) UUID selectedSkillId,
                                                       @RequestParam(value = "selectedSkillLevel", required = false) SkillLevelEnum selectedSkillLevel,
                                                       @RequestParam(value = "selectedQuestionType", required = false) QuestionTypeEnum selectedQuestionType,
                                                       @RequestParam(value = "sortOrder") String sortOrder,
                                                   @RequestParam(value = "sortBy") String sortBy,
                                                   @RequestParam(value = "pageNumber") Integer pageNumber,
                                                   @RequestParam(value = "pageSize") Integer pageSize) {
        Sort sort = Sort.by("DESC".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return questionService.findQuestionByContent(questionContent, selectedSkillId, selectedSkillLevel, selectedQuestionType, sort, pageNumber, pageSize);
    }

    @GetMapping("/getDashboardData")
    public List<DashboardDto> getDashboardData() {
        return questionService.getDashboardData();
    }


    @PutMapping("/{questionId}")
    public  void updateQuestion(@PathVariable("questionId") UUID questionId, @RequestBody EditQuestionDto editQuestionDto) {
        questionService.updateQuestion(questionId, editQuestionDto);
    }

    @PostMapping()
    public void saveQuestions(@RequestBody SaveQuestionDto saveQuestionDto) {
        questionService.saveQuestions(saveQuestionDto);
    }

    @DeleteMapping("/{questionId}")
    public void deleteQuestion(@PathVariable("questionId") UUID questionId) {
        questionService.deleteQuestion(questionId);
    }


}