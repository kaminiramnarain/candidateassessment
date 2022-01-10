package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.LoginDto;
import ch.elca.candidateassess.dto.PersonDto;
import ch.elca.candidateassess.dto.ReviewerDto;
import ch.elca.candidateassess.persistence.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface PersonMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    Person mapToPerson(PersonDto personDto);

    ReviewerDto mapToReviewerDto(Person person);

    void mapToLoginDto(@MappingTarget LoginDto loginDto, Person person);

}