package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.CreateCandidateAnswerDto;
import ch.elca.candidateassess.dto.SaveCandidateMarksDto;
import ch.elca.candidateassess.service.CandidateAnswerService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/candidate-answers")
public class CandidateAnswerController {

    private final CandidateAnswerService candidateAnswerService;

    public CandidateAnswerController(CandidateAnswerService candidateAnswerService) {
        this.candidateAnswerService = candidateAnswerService;
    }

    @PostMapping
    public void createCandidateAnswer(@RequestBody CreateCandidateAnswerDto createCandidateAnswerDto) {
        candidateAnswerService.saveCandidateAnswer(createCandidateAnswerDto);
    }

    @PutMapping("/saveMarks")
    public void saveCandidateMarks(@RequestBody SaveCandidateMarksDto saveCandidateMarksDto) {
        candidateAnswerService.saveCandidateMarks(saveCandidateMarksDto);
    }

    @PutMapping("/finish")
    public void finishQuestionnaire(@RequestParam UUID userQuestionnaireId, @RequestParam Integer remainingTime) {
        candidateAnswerService.finishQuestionnaire(userQuestionnaireId, remainingTime);
    }

}