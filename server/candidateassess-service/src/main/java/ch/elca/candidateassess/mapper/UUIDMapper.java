package ch.elca.candidateassess.mapper;

import org.mapstruct.Mapper;

import java.util.UUID;

/**
 * @author akn
 */
@Mapper(componentModel = "spring")
public interface UUIDMapper {

    default String mapFromUUID(UUID uuid) {
        return uuid.toString();
    }

    default UUID mapToUUID(String uuid) {
        return UUID.fromString(uuid);
    }

}