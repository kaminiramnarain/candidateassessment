package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.CreatePositionDto;
import ch.elca.candidateassess.dto.PositionDto;
import ch.elca.candidateassess.mapper.PositionMapper;
import ch.elca.candidateassess.persistence.entity.Position;
import ch.elca.candidateassess.persistence.entity.QPosition;
import ch.elca.candidateassess.persistence.entity.QUserQuestionnaire;
import ch.elca.candidateassess.persistence.entity.UserQuestionnaireSkill;
import ch.elca.candidateassess.persistence.repository.PositionRepository;
import ch.elca.candidateassess.service.PositionService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    @Override
    public void savePosition(CreatePositionDto createPositionDto) {
        Position position = positionMapper.mapToPosition(createPositionDto);
        positionRepository.save(position);
    }

    @Override
    public List<PositionDto> getPositions() {
        return positionRepository.findAll().stream().map(position -> positionMapper.mapToPositionDto(position)).collect(Collectors.toList());
    }

    @Override
    public List<PositionDto> searchPositionsByName(String positionName) {
        BooleanBuilder searchPredicate = buildSearchPredicate(positionName);
        List<Position> positions = new ArrayList<Position>();
        positionRepository.findAll(searchPredicate).forEach(positions::add);
        return positions.stream().map(position -> positionMapper.mapToPositionDto(position)).collect(Collectors.toList());
    }

    private BooleanBuilder buildSearchPredicate(String positionName) {
        var qPosition = QPosition.position;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qPosition.name.toLowerCase().contains(positionName.toLowerCase(Locale.ROOT)));
    }
}