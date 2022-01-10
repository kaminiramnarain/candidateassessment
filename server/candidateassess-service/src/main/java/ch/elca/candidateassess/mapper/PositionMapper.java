package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.CreatePositionDto;
import ch.elca.candidateassess.dto.PositionDto;
import ch.elca.candidateassess.persistence.entity.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface PositionMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    Position mapToPosition(CreatePositionDto createPositionDto);

    PositionDto mapToPositionDto(Position position);

}