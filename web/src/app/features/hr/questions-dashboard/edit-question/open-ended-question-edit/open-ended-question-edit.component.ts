import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EditQuestionDto, QuestionTypeEnum, Skill, SkillDto, SkillLevelEnum } from 'src/app/shared/model';
import { SkillService } from 'src/app/shared/service/api/skill/skill.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';

@Component({
  selector: 'app-open-ended-question-edit',
  templateUrl: './open-ended-question-edit.component.html',
  styleUrls: ['./open-ended-question-edit.component.scss']
})
export class OpenEndedQuestionEditComponent implements OnInit {

  @Input() question!: EditQuestionDto;
  public searchValueSkill!: string;
  public skillList!: SkillDto[];
  public selectedQuestionLevel!: number;
  public formGroup!: FormGroup;
  public skillLevelList: any = Object.keys(SkillLevelEnum).map(skillLevel =>
    skillLevel = this.utilsService.titleCase(skillLevel.replace("_", " "))
  ).filter(String);
  public selectedSkillLevel!: string;
  public quillConfig = {
    blotFormatter: {
    },
    toolbar: {
      container: [
        ['bold', 'italic', 'underline'],
        ['code-block'],
        [{ list: 'ordered' }, { list: 'bullet' }],
        [{ align: [] }],
        ['link', 'image'],
      ],
    },
  };

  constructor(private skillService: SkillService, private utilsService: UtilsService) { }

  ngOnInit(): void {
    this.formGroup = new FormGroup({
      skillId: new FormControl('', Validators.required),
      skillLevel: new FormControl('', Validators.required),
      marks: new FormControl('', Validators.required),
      duration: new FormControl('', Validators.required)
    });
    this.question.questionType = QuestionTypeEnum.OPEN_ENDED;
    this.setSkillLevel();
    this.skillService.getSkills('').subscribe((skill) => {
      this.skillList = skill;
    });
  }

  public onClose(): void {
    this.searchValueSkill = "";
    this.getSkills();
  }

  public getSkills(): void{
    this.skillService.getSkills('').subscribe((skill) => {
      this.skillList = skill;
    });
  }

  public setSkillLevel(): void {
    if (this.question.level == "NOVICE") {
      this.selectedSkillLevel = "Novice";
    } else if (this.question.level == "BEGINNER") {
      this.selectedSkillLevel = "Beginner";
    } else if (this.question.level == "PROFICIENT") {
      this.selectedSkillLevel = "Proficient";
    } else if (this.question.level == "ADVANCED") {
      this.selectedSkillLevel = "Advanced";
    } else if (this.question.level == "EXPERT") {
      this.selectedSkillLevel = "Expert";
    }
  }

  public changeSkillLevel(event: any): void {
    if (event.value == "Novice") {
      this.question.level = SkillLevelEnum.NOVICE;
    } else if (event.value == "Beginner") {
      this.question.level = SkillLevelEnum.BEGINNER;
    } else if (event.value == "Proficient") {
      this.question.level = SkillLevelEnum.PROFICIENT;
    } else if (event.value == "Advanced") {
      this.question.level = SkillLevelEnum.ADVANCED;
    } else if (event.value == "Expert") {
      this.question.level = SkillLevelEnum.EXPERT;
    }
  }

  public reviewerOnKey(event: any): void {
    this.searchValueSkill = event.target.value;
    this.skillService
      .getSkills(this.searchValueSkill)
      .subscribe((skill) => {
        this.skillList.length = 0;
        skill.forEach((skillResult) => {
          this.skillList.push(skillResult);
        });
      });
  }
}