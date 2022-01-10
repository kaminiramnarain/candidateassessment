package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.exception.ResourceNotFoundException;
import ch.elca.candidateassess.mapper.*;
import ch.elca.candidateassess.persistence.entity.*;
import ch.elca.candidateassess.persistence.enumeration.QuestionnaireStatusEnum;
import ch.elca.candidateassess.persistence.repository.PositionRepository;
import ch.elca.candidateassess.persistence.repository.ReviewRepository;
import ch.elca.candidateassess.persistence.repository.UserQuestionnaireRepository;
import ch.elca.candidateassess.persistence.repository.UserQuestionnaireSkillRepository;
import ch.elca.candidateassess.service.CandidateService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final PositionRepository positionRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final CandidateMapper candidateMapper;
    private final UserQuestionnaireRepository userQuestionnaireRepository;
    private final UUIDMapper uuidMapper;
    private final UserQuestionnaireSkillRepository userQuestionnaireSkillRepository;
    private final SkillMapper skillMapper;
    private final UserQuestionnaireSkillMapper userQuestionnaireSkillMapper;


    public CandidateServiceImpl(PositionRepository positionRepository, CandidateMapper candidateMapper, UserQuestionnaireRepository userQuestionnaireRepository, UUIDMapper uuidMapper, UserQuestionnaireSkillRepository userQuestionnaireSkillRepository, SkillMapper skillMapper, ReviewRepository reviewRepository, ReviewMapper reviewMapper, UserQuestionnaireSkillMapper userQuestionnaireSkillMapper) {
        this.positionRepository = positionRepository;
        this.candidateMapper = candidateMapper;
        this.userQuestionnaireRepository = userQuestionnaireRepository;
        this.uuidMapper = uuidMapper;
        this.userQuestionnaireSkillRepository = userQuestionnaireSkillRepository;
        this.skillMapper = skillMapper;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.userQuestionnaireSkillMapper = userQuestionnaireSkillMapper;

    }

    @Override
    public Page<CandidateDto> getCandidates(Sort sort, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        BooleanBuilder candidatePredicate = buildCandidatePredicate();
        Page<UserQuestionnaire> userQuestionnaires = userQuestionnaireRepository.findAll(candidatePredicate, pageRequest);
        Page<CandidateDto> candidates = userQuestionnaires.map(userQuestionnaire -> {
            CandidateDto candidateDto = candidateMapper.mapToCandidateDto(userQuestionnaire);
            UUID candidateDtoId = candidateDto.getId();
            BooleanBuilder reviewPredicate = buildReviewPredicate(candidateDtoId);
            List<Review> review = new ArrayList<Review>();
            reviewRepository.findAll(reviewPredicate).forEach(review::add);
            List<ReviewerDto> reviewers = review.stream().map(reviewDto -> {
                ReviewerDto reviewer = new ReviewerDto();
                reviewer = reviewMapper.mapToReviewerDto(reviewDto);
                return reviewer;
            }).collect(Collectors.toList());
            candidateDto.setReviewers(reviewers);
            BooleanBuilder skillPredicate = buildSkillPredicate(candidateDtoId);
            List<UserQuestionnaireSkill> skill = new ArrayList<UserQuestionnaireSkill>();
            userQuestionnaireSkillRepository.findAll(skillPredicate).forEach(skill::add);
            List<SelectedSkillDto> selectedSkillDtos = skill.stream()
                    .map(userQuestionnaireSkillMapper::mapToSelectedSkillDto).collect(Collectors.toList());
            candidateDto.setSkills(selectedSkillDtos);
            return candidateDto;
        });
        return candidates;
    }

    @Override
    public Page<CandidateDto> getCandidatesForCustomizedQuestionnaires(UUID personId, Sort sort, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        BooleanBuilder candidatePredicate = buildCandidatesForCustomizedQuestionnairesPredicate(personId);
        Page<Review> reviewList = reviewRepository.findAll(candidatePredicate, pageRequest);
        Page<CandidateDto> candidates = reviewList.map(review -> {
            CandidateDto candidateDto = candidateMapper.mapToCandidateDto(review.getUserQuestionnaire());
            UUID candidateDtoId = candidateDto.getId();
            BooleanBuilder reviewPredicate = buildReviewPredicate(candidateDtoId);
            List<Review> reviews = new ArrayList<Review>();
            reviewRepository.findAll(reviewPredicate).forEach(reviews::add);
            List<ReviewerDto> reviewers = reviews.stream().map(reviewDto -> {
                ReviewerDto reviewer = new ReviewerDto();
                reviewer = reviewMapper.mapToReviewerDto(reviewDto);
                return reviewer;
            }).collect(Collectors.toList());
            candidateDto.setReviewers(reviewers);
            BooleanBuilder skillPredicate = buildSkillPredicate(candidateDtoId);
            List<UserQuestionnaireSkill> skill = new ArrayList<UserQuestionnaireSkill>();
            userQuestionnaireSkillRepository.findAll(skillPredicate).forEach(skill::add);
            List<SelectedSkillDto> selectedSkillDtos = skill.stream()
                    .map(userQuestionnaireSkillMapper::mapToSelectedSkillDto).collect(Collectors.toList());
            candidateDto.setSkills(selectedSkillDtos);
            return candidateDto;
        });
        return candidates;
    }

    private BooleanBuilder buildCandidatesForCustomizedQuestionnairesPredicate(UUID personId) {
        var qReview = QReview.review;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qReview.person.id.eq(personId))
                .and(qReview.userQuestionnaire.userArchived.eq(false))
                .and(qReview.userQuestionnaire.status.eq(QuestionnaireStatusEnum.QUESTIONNAIRE_NOT_GENERATED))
                .and(qReview.userQuestionnaire.autoGenerate.eq(false));
    }

    private BooleanBuilder buildSearchCandidatesForCustomizedQuestionnairesPredicate(UUID personId, String candidateName) {
        var qReview = QReview.review;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qReview.person.id.eq(personId))
                .and(qReview.userQuestionnaire.userArchived.eq(false))
                .and(qReview.userQuestionnaire.status.eq(QuestionnaireStatusEnum.QUESTIONNAIRE_NOT_GENERATED))
                .and(qReview.userQuestionnaire.autoGenerate.eq(false))
                .and((qReview.userQuestionnaire.firstName.concat(" ").concat(qReview.userQuestionnaire.lastName).toLowerCase().contains(candidateName.toLowerCase(Locale.ROOT)))
                        .or(qReview.userQuestionnaire.lastName.concat(" ").concat(qReview.userQuestionnaire.firstName).toLowerCase().contains(candidateName.toLowerCase(Locale.ROOT))));

    }

    @Override
    public Page<CandidateDto> searchCandidatesForCustomizedQuestionnaires(UUID personId, String candidateName, Sort sort, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        BooleanBuilder candidatePredicate = buildSearchCandidatesForCustomizedQuestionnairesPredicate(personId, candidateName);
        Page<Review> reviewList = reviewRepository.findAll(candidatePredicate, pageRequest);
        Page<CandidateDto> candidates = reviewList.map(review -> {
            CandidateDto candidateDto = candidateMapper.mapToCandidateDto(review.getUserQuestionnaire());
            UUID candidateDtoId = candidateDto.getId();
            BooleanBuilder reviewPredicate = buildReviewPredicate(candidateDtoId);
            List<Review> reviews = new ArrayList<Review>();
            reviewRepository.findAll(reviewPredicate).forEach(reviews::add);
            List<ReviewerDto> reviewers = reviews.stream().map(reviewDto -> {
                ReviewerDto reviewer = new ReviewerDto();
                reviewer = reviewMapper.mapToReviewerDto(reviewDto);
                return reviewer;
            }).collect(Collectors.toList());
            candidateDto.setReviewers(reviewers);
            BooleanBuilder skillPredicate = buildSkillPredicate(candidateDtoId);
            List<UserQuestionnaireSkill> skill = new ArrayList<UserQuestionnaireSkill>();
            userQuestionnaireSkillRepository.findAll(skillPredicate).forEach(skill::add);
            List<SelectedSkillDto> selectedSkillDtos = skill.stream()
                    .map(userQuestionnaireSkillMapper::mapToSelectedSkillDto).collect(Collectors.toList());
            candidateDto.setSkills(selectedSkillDtos);
            return candidateDto;
        });
        return candidates;
    }

    @Override
    public List<CandidatesWhoAreNotAssignedInterviewDateDto> getCandidatesWhoAreNotAssignedInterviewDate() {
        BooleanBuilder noInterviewCandidatePredicate = buildNoInterviewCandidatePredicate();
        List<UserQuestionnaire> userQuestionnaires = new ArrayList<UserQuestionnaire>();
        userQuestionnaireRepository.findAll(noInterviewCandidatePredicate).forEach(userQuestionnaires::add);
        return userQuestionnaires.stream().map(candidateMapper::mapToCandidatesWhoAreNotAssignedInterviewDateDto).collect(Collectors.toList());
    }

    private BooleanBuilder buildNoInterviewCandidatePredicate() {
        var qUserQuestionnaire = QUserQuestionnaire.userQuestionnaire;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qUserQuestionnaire.userArchived.eq(false)).and(qUserQuestionnaire.interviewDate.isNull());
    }

    @Override
    public CandidateReviewDto getCandidateDataForReview(String candidateId) {
        UserQuestionnaire userQuestionnaire = userQuestionnaireRepository.getById(uuidMapper.mapToUUID(candidateId));
        return candidateMapper.mapToCandidateReviewDto(userQuestionnaire);
    }

    @Override
    public List<CandidatesWhoAreNotAssignedInterviewDateDto> findCandidatesWhoAreNotAssignedInterviewDateByName(String candidateName) {
        BooleanBuilder predicate = buildSearchCandidatesWhoAreNotAssignedInterviewDatePredicate(candidateName);
        List<UserQuestionnaire> userQuestionnaires = new ArrayList<UserQuestionnaire>();
        userQuestionnaireRepository.findAll(predicate).forEach(userQuestionnaires::add);
        return userQuestionnaires.stream().map(candidateMapper::mapToCandidatesWhoAreNotAssignedInterviewDateDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CandidateDto> getCandidateById(String candidateId) {
        Optional<UserQuestionnaire> userQuestionnaires = userQuestionnaireRepository.findById(uuidMapper.mapToUUID(candidateId));
        return userQuestionnaires.stream().map(userQuestionnaire -> {
            CandidateDto candidateDto = candidateMapper.mapToCandidateDto(userQuestionnaire);
            UUID candidateDtoId = candidateDto.getId();
            BooleanBuilder reviewPredicate = buildReviewPredicate(candidateDtoId);
            List<Review> review = new ArrayList<Review>();
            reviewRepository.findAll(reviewPredicate).forEach(review::add);
            List<ReviewerDto> reviewers = review.stream().map(reviewDto -> {
                ReviewerDto reviewer = new ReviewerDto();
                reviewer = reviewMapper.mapToReviewerDto(reviewDto);
                return reviewer;
            }).collect(Collectors.toList());
            candidateDto.setReviewers(reviewers);
            BooleanBuilder skillPredicate = buildSkillPredicate(candidateDtoId);
            List<UserQuestionnaireSkill> skill = new ArrayList<UserQuestionnaireSkill>();
            userQuestionnaireSkillRepository.findAll(skillPredicate).forEach(skill::add);
            List<SelectedSkillDto> selectedSkillDtos = skill.stream()
                    .map(userQuestionnaireSkillMapper::mapToSelectedSkillDto).collect(Collectors.toList());
            candidateDto.setSkills(selectedSkillDtos);
            return candidateDto;
        }).findFirst();
    }

    private BooleanBuilder buildCandidatePredicate() {
        var qUserQuestionnaire = QUserQuestionnaire.userQuestionnaire;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qUserQuestionnaire.userArchived.eq(false));
    }

    private BooleanBuilder buildSkillPredicate(UUID candidateId) {
        var qUserQuestionnaireSkill = QUserQuestionnaireSkill.userQuestionnaireSkill;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qUserQuestionnaireSkill.userQuestionnaire.id.eq(candidateId));
    }


    @Override
    public Page<CandidateDto> findCandidatesByName(String candidateName, Sort sort, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        BooleanBuilder predicate = buildSearchPredicate(candidateName);
        Page<UserQuestionnaire> userQuestionnaires = userQuestionnaireRepository.findAll(predicate, pageRequest);
        Page<CandidateDto> candidates = userQuestionnaires.map(userQuestionnaire -> {
            CandidateDto candidateDto = candidateMapper.mapToCandidateDto(userQuestionnaire);
            UUID candidateDtoId = candidateDto.getId();
            BooleanBuilder skillPredicate = buildSkillPredicate(candidateDtoId);
            List<UserQuestionnaireSkill> skill = new ArrayList<UserQuestionnaireSkill>();
            userQuestionnaireSkillRepository.findAll(skillPredicate).forEach(skill::add);
            List<SelectedSkillDto> selectedSkillDtos = skill.stream()
                    .map(userQuestionnaireSkillMapper::mapToSelectedSkillDto).collect(Collectors.toList());
            candidateDto.setSkills(selectedSkillDtos);
            return candidateDto;
        });
        return candidates;
    }

    private BooleanBuilder buildReviewPredicate(UUID candidateId) {
        var qReview = QReview.review;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qReview.userQuestionnaire.id.eq(candidateId));
    }

    private BooleanBuilder buildSearchCandidatesWhoAreNotAssignedInterviewDatePredicate(String candidateName) {
        var qUserQuestionnaire = QUserQuestionnaire.userQuestionnaire;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qUserQuestionnaire.userArchived.eq(false)).and(qUserQuestionnaire.interviewDate.isNull()).and((qUserQuestionnaire.firstName.concat(" ").concat(qUserQuestionnaire.lastName).toLowerCase().contains(candidateName.toLowerCase(Locale.ROOT)))
                .or(qUserQuestionnaire.lastName.concat(" ").concat(qUserQuestionnaire.firstName).toLowerCase().contains(candidateName.toLowerCase(Locale.ROOT))));
    }

    private BooleanBuilder buildSearchPredicate(String candidateName) {
        var qUserQuestionnaire = QUserQuestionnaire.userQuestionnaire;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qUserQuestionnaire.userArchived.eq(false)).and((qUserQuestionnaire.firstName.concat(" ").concat(qUserQuestionnaire.lastName).toLowerCase().contains(candidateName.toLowerCase(Locale.ROOT)))
                .or(qUserQuestionnaire.lastName.concat(" ").concat(qUserQuestionnaire.firstName).toLowerCase().contains(candidateName.toLowerCase(Locale.ROOT))));
    }

    @Override
    public void markCandidateAsArchived(String candidateId) {
        userQuestionnaireRepository.findById(uuidMapper.mapToUUID(candidateId)).ifPresentOrElse(candidate -> {
            candidate.setUserArchived(true);
            userQuestionnaireRepository.save(candidate);
        }, () -> {
            throw new ResourceNotFoundException("Candidate not found!");
        });
    }
}
