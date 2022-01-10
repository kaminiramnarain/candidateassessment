package ch.elca.candidateassess.rest;

import ch.elca.candidateassess.dto.CreateSkillDto;
import ch.elca.candidateassess.dto.SkillDto;
import ch.elca.candidateassess.service.SkillService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public List<SkillDto> searchSkills(@RequestParam(required = false) String skillName) {
        return skillService.searchSkills(skillName);
    }

    @PostMapping
    public void createSkill(@RequestBody CreateSkillDto createSkillDto) {
        skillService.saveSkill(createSkillDto);
    }

}