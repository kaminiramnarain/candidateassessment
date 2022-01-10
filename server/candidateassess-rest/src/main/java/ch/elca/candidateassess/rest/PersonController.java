package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.CredentialsDto;
import ch.elca.candidateassess.dto.LoginDto;
import ch.elca.candidateassess.dto.PersonDto;
import ch.elca.candidateassess.dto.ReviewerDto;
import ch.elca.candidateassess.service.PersonService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/reviewers")
    public List<ReviewerDto> getReviewers() {
        return personService.getReviewers();
    }

    @GetMapping("reviewers/search")
    public List<ReviewerDto> getReviewersByName(@RequestParam(value = "reviewerName", required = false) String reviewerName) {
        return personService.getReviewersByName(reviewerName);
    }

    @PostMapping("/validateCredentials")
    public LoginDto validateCredentials(@RequestBody CredentialsDto credentialsDto) {
        return personService.validateCredentials(credentialsDto);
    }

    @PostMapping
    public void createPerson(@RequestBody PersonDto personDto) {
        personService.savePerson(personDto);
    }

}