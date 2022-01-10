package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.CreateReviewDto;
import ch.elca.candidateassess.dto.ReviewerDto;
import ch.elca.candidateassess.persistence.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface ReviewMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "userQuestionnaire", ignore = true)
    Review mapToReview(CreateReviewDto createReviewDto);

    @Mapping(source = "review.person.firstName", target = "firstName")
    @Mapping(source = "review.person.lastName", target = "lastName")
    ReviewerDto mapToReviewerDto(Review review);

}