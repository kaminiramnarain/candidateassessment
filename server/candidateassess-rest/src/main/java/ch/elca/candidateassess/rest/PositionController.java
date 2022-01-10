package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.CreatePositionDto;
import ch.elca.candidateassess.dto.PositionDto;
import ch.elca.candidateassess.service.PositionService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public List<PositionDto> getPosition() {
        return positionService.getPositions();
    }

    @GetMapping("/search")
    public List<PositionDto> searchPositionsByName(@RequestParam(value = "positionName", required = false) String positionName) {
        return positionService.searchPositionsByName(positionName);
    }

    @PostMapping
    public void createPosition(@RequestBody CreatePositionDto createPositionDto) {
        positionService.savePosition(createPositionDto);
    }


}

