import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MultipleAnswerDto, NewQuestion, QuestionTypeEnum, SkillDto, SkillLevelEnum } from 'src/app/shared/model';
import { SkillService } from 'src/app/shared/service/api/skill/skill.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';
import { v4 as uuid } from 'uuid';

@Component({
  selector: 'app-multiple-answers-question-create',
  templateUrl: './multiple-answers-question.component.html',
  styleUrls: ['./multiple-answers-question.component.scss']
})
export class MultipleAnswersQuestionComponent implements OnInit {

  @Input() question!: NewQuestion;
  public searchValueSkill!: string;
  public skillList!: SkillDto[];
  public formGroup!: FormGroup;
  public skillLevelInt!: number;
  public isAddButtonDisabled: boolean = false;
  public isRemoveButtonDisabled: boolean = true;
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
    })
    this.question.multipleAnswers = [new MultipleAnswerDto(uuid(), false), new MultipleAnswerDto(uuid(), false), new MultipleAnswerDto(uuid(), false)];
    this.question.questionType = QuestionTypeEnum.MULTIPLE_ANSWERS;
    this.skillService.getSkills('').subscribe((skill) => {
      this.skillList = skill;
    })
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

  public selectionChanged(event: any): void {
    this.question.level = event.value.toUpperCase();
  }


  public addAnswer(): void {
    this.question.multipleAnswers.push(new MultipleAnswerDto(uuid(), false));
    if (this.question.multipleAnswers.length == 5) {
      this.isAddButtonDisabled = true;
    } else {
      this.isAddButtonDisabled = false;
    }

    if (this.question.multipleAnswers.length > 3) {
      this.isRemoveButtonDisabled = false;
    } else {
      this.isRemoveButtonDisabled = true;
    }
  }
  public changeValueOfAnswer(event: any, answerId: string): void {
    this.question.multipleAnswers.forEach(answer => {
      if (answer.id == answerId) answer.value = event.checked;
    });
  }

  public removeAnswer(answerId: string): void {
    this.question.multipleAnswers = this.question.multipleAnswers.filter(answer => answer.id != answerId);
    if (this.question.multipleAnswers.length == 5) {
      this.isAddButtonDisabled = true;
    } else {
      this.isAddButtonDisabled = false;
    }
    if (this.question.multipleAnswers.length > 3) {
      this.isRemoveButtonDisabled = false;
    } else {
      this.isRemoveButtonDisabled = true;
    }
  }

  public onChangeLevel(event: any): void {
    var skillLevel = event.target.value;

    if (skillLevel == 1) {
      this.question.level = SkillLevelEnum.NOVICE;
    } else if (skillLevel == 2) {
      this.question.level = SkillLevelEnum.BEGINNER;
    } else if (skillLevel == 3) {
      this.question.level = SkillLevelEnum.PROFICIENT;
    } else if (skillLevel == 4) {
      this.question.level = SkillLevelEnum.ADVANCED;
    } else if (skillLevel == 5) {
      this.question.level = SkillLevelEnum.EXPERT;
    }

  }

}
