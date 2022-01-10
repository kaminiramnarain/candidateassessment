package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.CandidateDto;
import ch.elca.candidateassess.dto.CandidateReviewDto;
import ch.elca.candidateassess.dto.CreateReviewDto;
import ch.elca.candidateassess.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public void createReview(@RequestBody CreateReviewDto createReviewDto) {
        reviewService.saveReview(createReviewDto);
    }

    @GetMapping("getUserQuestionnaireData/{personId}")
    public Page<CandidateReviewDto> getUserQuestionnaireData(@RequestParam(value = "sortOrder") String sortOrder,
                                                             @RequestParam(value = "sortBy") String sortBy,
                                                             @RequestParam(value = "pageNumber") Integer pageNumber,
                                                             @RequestParam(value = "pageSize") Integer pageSize,
                                                             @PathVariable UUID personId) {
        Sort sort = Sort.by("DESC".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return reviewService.getUserQuestionnaireData(sort, pageNumber, pageSize, personId);
    }

    @GetMapping("getUserQuestionnaireData/{personId}/search")
    public Page<CandidateReviewDto> searchByCandidateName(@RequestParam(value = "candidateName", required = false) String candidateName,
                                                    @RequestParam(value = "sortOrder") String sortOrder,
                                                    @RequestParam(value = "sortBy") String sortBy,
                                                    @RequestParam(value = "pageNumber") Integer pageNumber,
                                                    @RequestParam(value = "pageSize") Integer pageSize,
                                                    @PathVariable UUID personId) {
        Sort sort = Sort.by("DESC".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        return reviewService.searchByCandidateName(candidateName, sort, pageNumber, pageSize, personId);
    }


}