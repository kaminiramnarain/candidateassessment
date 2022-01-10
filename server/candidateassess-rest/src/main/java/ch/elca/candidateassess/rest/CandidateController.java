package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.CandidateDto;
import ch.elca.candidateassess.dto.CandidateReviewDto;
import ch.elca.candidateassess.dto.CandidatesWhoAreNotAssignedInterviewDateDto;
import ch.elca.candidateassess.service.CandidateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping()
    public Page<CandidateDto> getCandidates(@RequestParam(value = "sortOrder") String sortOrder,
                                            @RequestParam(value = "sortBy") String sortBy,
                                            @RequestParam(value = "pageNumber") Integer pageNumber,
                                            @RequestParam(value = "pageSize") Integer pageSize) {
        Sort sort = Sort.by("DESC".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return candidateService.getCandidates(sort, pageNumber, pageSize);
    }

    @GetMapping("/customizeQuestionnaire/{personId}")
    public Page<CandidateDto> getCandidatesForCustomizedQuestionnaires(@PathVariable("personId") UUID personId,
                                                                       @RequestParam(value = "sortOrder") String sortOrder,
                                            @RequestParam(value = "sortBy") String sortBy,
                                            @RequestParam(value = "pageNumber") Integer pageNumber,
                                            @RequestParam(value = "pageSize") Integer pageSize) {
        Sort sort = Sort.by("DESC".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return candidateService.getCandidatesForCustomizedQuestionnaires(personId, sort, pageNumber, pageSize);
    }

    @GetMapping("/customizeQuestionnaire/{personId}/search")
    public Page<CandidateDto> searchCandidatesForCustomizedQuestionnaires(@PathVariable("personId") UUID personId,
                                                                          @RequestParam(value = "candidateName", required = false) String candidateName,
                                                                       @RequestParam(value = "sortOrder") String sortOrder,
                                                                       @RequestParam(value = "sortBy") String sortBy,
                                                                       @RequestParam(value = "pageNumber") Integer pageNumber,
                                                                       @RequestParam(value = "pageSize") Integer pageSize) {
        Sort sort = Sort.by("DESC".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return candidateService.searchCandidatesForCustomizedQuestionnaires(personId, candidateName, sort, pageNumber, pageSize);
    }

    @GetMapping("/noInterviewDate")
    public List<CandidatesWhoAreNotAssignedInterviewDateDto> getCandidatesWhoAreNotAssignedInterviewDate() {
        return candidateService.getCandidatesWhoAreNotAssignedInterviewDate();
    }

    @GetMapping("/forReview/{candidateId}")
    public CandidateReviewDto getCandidateDataForReview(@PathVariable("candidateId") String candidateId) {
        return candidateService.getCandidateDataForReview(candidateId);
    }

    @GetMapping("/noInterviewDate/search")
    public List<CandidatesWhoAreNotAssignedInterviewDateDto> findCandidatesWhoAreNotAssignedInterviewDateByName(@RequestParam(value = "candidateName", required = false) String candidateName) {
        return candidateService.findCandidatesWhoAreNotAssignedInterviewDateByName(candidateName);
    }

    @GetMapping("/{candidateId}")
    public Optional<CandidateDto> getCandidateById(@PathVariable("candidateId") String candidateId) {
        return candidateService.getCandidateById(candidateId);
    }

    @GetMapping("/search")
    public Page<CandidateDto> findCandidatesByName(@RequestParam(value = "candidateName", required = false) String candidateName,
                                                   @RequestParam(value = "sortOrder") String sortOrder,
                                                   @RequestParam(value = "sortBy") String sortBy,
                                                   @RequestParam(value = "pageNumber") Integer pageNumber,
                                                   @RequestParam(value = "pageSize") Integer pageSize) {
        System.out.println(sortBy);
        Sort sort = Sort.by("DESC".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return candidateService.findCandidatesByName(candidateName, sort, pageNumber, pageSize);
    }

    @PutMapping("/archive/{candidateId}")
    public void markCandidateAsArchived(@PathVariable("candidateId") String candidateId) {
        candidateService.markCandidateAsArchived(candidateId);
    }

}
