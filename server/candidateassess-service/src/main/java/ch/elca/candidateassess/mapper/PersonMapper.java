package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.LoginDto;
import ch.elca.candidateassess.dto.PersonDto;
import ch.elca.candidateassess.dto.ReviewerDto;
import ch.elca.candidateassess.persistence.entity.Person;
import ch.elca.candidateassess.persistence.entity.UserQuestionnaire;
import org.mapstruct.Mapper;
<<<<<<< HEAD
=======
import org.mapstruct.Mapping;
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface PersonMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    Person mapToPerson(PersonDto personDto);

    ReviewerDto mapToReviewerDto(Person person);

    void mapToLoginDto(@MappingTarget LoginDto loginDto, Person person);

<<<<<<< HEAD
    void mapToLoginDto(@MappingTarget LoginDto loginDto, Person person);


}
=======
}
>>>>>>> d2f92d0d4fb8ec9b890d8dc9842b4ac40634f325
