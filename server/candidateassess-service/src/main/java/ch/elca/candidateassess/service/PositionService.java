package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.CreatePositionDto;
import ch.elca.candidateassess.dto.PositionDto;

import java.util.List;

public interface PositionService {

    void savePosition(CreatePositionDto createPositionDto);

    List<PositionDto> getPositions();

    List<PositionDto> searchPositionsByName(String positionName);

}

