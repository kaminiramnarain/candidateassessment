package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.CandidateReviewDto;
import ch.elca.candidateassess.dto.CreateReviewDto;
import ch.elca.candidateassess.exception.ResourceNotFoundException;
import ch.elca.candidateassess.mapper.CandidateMapper;
import ch.elca.candidateassess.mapper.ReviewMapper;
import ch.elca.candidateassess.mapper.UUIDMapper;
import ch.elca.candidateassess.persistence.entity.QReview;
import ch.elca.candidateassess.persistence.entity.QUserQuestionnaire;
import ch.elca.candidateassess.persistence.entity.Review;
import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import ch.elca.candidateassess.persistence.repository.PersonRepository;
import ch.elca.candidateassess.persistence.repository.ReviewRepository;
import ch.elca.candidateassess.persistence.repository.UserQuestionnaireRepository;
import ch.elca.candidateassess.service.ReviewService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {


    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserQuestionnaireRepository userQuestionnaireRepository;
    private final PersonRepository personRepository;
    private final UUIDMapper uuidMapper;
    private final CandidateMapper candidateMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CandidateMapper candidateMapper, ReviewMapper reviewMapper, UserQuestionnaireRepository userQuestionnaireRepository, PersonRepository personRepository, UUIDMapper uuidMapper) {
        this.reviewRepository = reviewRepository;
        this.candidateMapper = candidateMapper;
        this.reviewMapper = reviewMapper;
        this.userQuestionnaireRepository = userQuestionnaireRepository;
        this.personRepository = personRepository;
        this.uuidMapper = uuidMapper;
    }

    @Override
    public void saveReview(CreateReviewDto createReviewDto) {
        createReviewDto.getPersonIds().stream().forEach(reviewerId -> {
            Review review = reviewMapper.mapToReview(createReviewDto);
            System.out.println(review);
            personRepository.findById(uuidMapper.mapToUUID(reviewerId)).ifPresentOrElse(review::setPerson, () -> {
                throw new ResourceNotFoundException("Person does not exist!");
            });
            userQuestionnaireRepository.findById(createReviewDto.getUserQuestionnaireId()).ifPresentOrElse(review::setUserQuestionnaire, () -> {
                throw new ResourceNotFoundException("User questionnaire does not exist!");
            });
            reviewRepository.save(review);
        });
    }


    @Override
    public Page<CandidateReviewDto> getUserQuestionnaireData(Sort sort, Integer pageNumber, Integer pageSize, UUID personId) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        BooleanBuilder reviewPredicate = buildPredicate(personId);
        Page<Review> reviews = reviewRepository.findAll(reviewPredicate, pageRequest);
        Page<CandidateReviewDto> candidateReviewDtos = reviews.map(review -> candidateMapper.mapToCandidateReviewDto(review.getUserQuestionnaire()));
        return candidateReviewDtos;
    }

    @Override
    public Page<CandidateReviewDto> searchByCandidateName(String candidateName, Sort sort, Integer pageNumber, Integer pageSize, UUID personId) {
        Page<CandidateReviewDto> candidateReviewDtos = getUserQuestionnaireData(sort, pageNumber, pageSize, personId);
        return new PageImpl<CandidateReviewDto>(candidateReviewDtos.stream().filter(candidateReviewDto -> candidateReviewDto.getFirstName().concat(" ").concat(candidateReviewDto.getLastName()).toLowerCase().contains(candidateName.toLowerCase(Locale.ROOT)) || candidateReviewDto.getLastName().concat(" ").concat(candidateReviewDto.getFirstName()).toLowerCase().contains(candidateName.toLowerCase(Locale.ROOT))).collect(Collectors.toList()));
    }

    private BooleanBuilder buildPredicate(UUID personId) {
        var qReview = QReview.review;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qReview.person.id.eq(personId)).and(qReview.userQuestionnaire.status.eq(QuestionnaireStatusEnum.UNDER_REVIEW));
    }

}

