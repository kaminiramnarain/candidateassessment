package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.PersonDto;
import ch.elca.candidateassess.dto.ReviewerDto;
import ch.elca.candidateassess.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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

    //
//    @PostMapping("/validateCredentials")
//    public LoginDto validateCredentials(@RequestBody CredentialsDto credentialsDto) {
//        return personService.validateCredentials(credentialsDto);
//    }
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return ResponseEntity.ok("redirect:/");
    }

    @PostMapping
    public void createPerson(@RequestBody PersonDto personDto) {
        personService.savePerson(personDto);
    }

}