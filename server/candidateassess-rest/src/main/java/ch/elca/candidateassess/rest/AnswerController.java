package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.AnswerDto;
import ch.elca.candidateassess.service.AnswerService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public void createAnswer(@RequestBody AnswerDto answerDto) {
        answerService.saveAnswer(answerDto);
    }

}