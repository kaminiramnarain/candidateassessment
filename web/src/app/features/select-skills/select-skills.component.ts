import {
  CdkDragDrop,
  moveItemInArray,
  transferArrayItem,
} from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { SkillDto, SkillLevelEnum, UserQuestionnaireSkillDto } from 'src/app/shared/model';
import { QuestionnaireService } from 'src/app/shared/service/api/questionnaire/questionnaire.service';
import { SkillService } from 'src/app/shared/service/api/skill/skill.service';
import { UserQuestionnaireService } from 'src/app/shared/service/api/user-questionnaire/user-questionnaire.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';

@Component({
  selector: 'app-select-skills',
  templateUrl: './select-skills.component.html',
  styleUrls: ['./select-skills.component.scss'],
})
export class SelectSkillsComponent implements OnInit {
  public userQuestionnaireId!: string;
  public searchValue!: string;
  public amountSelectedSkills: number = 0;
  public isContinueButtonDisabled: boolean = true;
  public isResetButtonDisabled: boolean = true;
  public loading: boolean = false;
  public skills: SkillDto[] = [];
  public filteredSkills: SkillDto[] = [];
  public selectedSkills: SkillDto[] = [];
  public candidateSelectSkills!: boolean;
  public autoGenerate!: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private skillService: SkillService,
    private userQuestionnaireService: UserQuestionnaireService,
    private utilService: UtilsService,
    private questionnaireService: QuestionnaireService
  ) {
    if (!(this.route.snapshot.params['id'] === undefined))
      this.userQuestionnaireId = this.route.snapshot.params['id'];
  }

  ngOnInit(): void {
    this.checkId();
    this.getQuestionnaireType();
    this.skillService.getSkills().subscribe((skills) => {
      skills.forEach((skill) => (skill.level = 1));
      this.skills = skills;
      this.filteredSkills = skills;
    });
    this.checkCandidateSelectSkillsStatus();
  }

  public getQuestionnaireType(): void {
    this.userQuestionnaireService.getQuestionnaireType(this.userQuestionnaireId).subscribe((autogenerate) =>
      this.autoGenerate = autogenerate
    )
  }

  public checkId(): void {
    this.userQuestionnaireService.validateId(this.userQuestionnaireId).subscribe({
      next: () => {
      },
      error: (error) => {
        this.router.navigate(['/page-not-found']);
      },
    });
  }

  private checkCandidateSelectSkillsStatus(): void {
    this.userQuestionnaireService.getCandidateSelectSkillsStatus(this.userQuestionnaireId).subscribe((dto) =>
      this.candidateSelectSkills = dto.candidateSelectSkills
    );
  }

  public valuechange(newValue: string) {
    this.filteredSkills = this.skills.filter((skill) =>
      skill.name.toLowerCase().includes(newValue.toLowerCase())
    );
  }

  public resetSearchValue(): void {
    this.searchValue = '';
    this.filteredSkills = this.skills;
  }

  public trackByIndex(index: number, obj: any): any {
    return index;
  }


  public drop(event: CdkDragDrop<SkillDto[]>, destination: string): void {

    if (event.previousContainer === event.container) {
      moveItemInArray(
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    } else {
      if (this.selectedSkills.length < 5 && destination === 'selection') {
        var previousIndex = -1;
        if (this.searchValue != undefined && this.searchValue != '') {

          for (var i = 0; i < this.skills.length; i++) {
            if (this.skills[i] == event.previousContainer.data[event.previousIndex]) {
              previousIndex = i;
            }
          }
          this.filteredSkills = this.filteredSkills.filter(skill => skill.id != this.skills[previousIndex].id);
          transferArrayItem(this.skills, this.selectedSkills, previousIndex, this.selectedSkills.length);
        } else {
          transferArrayItem(
            event.previousContainer.data,
            event.container.data,
            event.previousIndex,
            event.currentIndex
          );
        }



      } else if (destination === 'source') {
        transferArrayItem(
          event.previousContainer.data,
          event.container.data,
          event.previousIndex,
          event.currentIndex
        );
      }
    }
    this.amountSelectedSkills = this.selectedSkills.length;
    this.checkButtonStatus();
  }

  public resetSkills(): void {
    for (var skill of this.selectedSkills) {
      this.skills.push(skill);
    }
    this.selectedSkills = [];
    this.amountSelectedSkills = this.selectedSkills.length;
    this.checkButtonStatus();
  }

  public removeFromSelection(item: SkillDto): void {
    this.skills.push(item);
    this.selectedSkills = this.selectedSkills.filter((skill) => skill !== item);
    this.amountSelectedSkills = this.selectedSkills.length;
    this.checkButtonStatus();
  }

  private checkButtonStatus(): void {
    this.isResetButtonDisabled = this.amountSelectedSkills === 0 ? true : false;
    this.isContinueButtonDisabled =
      this.amountSelectedSkills < 3 ? true : false;
  }

  public submitSkills(): void {

    this.selectedSkills.forEach(skill => {
      if (skill.level == 1) {
        skill.level = SkillLevelEnum.NOVICE;
      } else if (skill.level == 2) {
        skill.level = SkillLevelEnum.BEGINNER;
      } else if (skill.level == 3) {
        skill.level = SkillLevelEnum.PROFICIENT;
      } else if (skill.level == 4) {
        skill.level = SkillLevelEnum.ADVANCED;
      } else if (skill.level == 5) {
        skill.level = SkillLevelEnum.EXPERT;
      }
    });

    var body = {
      userQuestionnaireId: this.userQuestionnaireId,
      skills: this.selectedSkills,
    } as UserQuestionnaireSkillDto;
    this.loading = true;
    this.isContinueButtonDisabled = true;
    this.isResetButtonDisabled = true;

    this.skillService.submitSkills(body).subscribe({
      next: (result) => {
        this.loading = false;
        this.checkButtonStatus();
        if (this.autoGenerate) {
          this.questionnaireService.generateQuestionnaire(this.userQuestionnaireId).subscribe(() => {
            this.loading = false;
            this.checkButtonStatus();
            if (this.candidateSelectSkills) {
              this.router.navigate([`/questionnaire-start/${this.userQuestionnaireId}`]);
            } else
              this.router.navigate([`/candidate-dashboard`]);
            this.utilService.openSnackBar('Skills saved successfully!', 'Dismiss');
          });
        } else {
          this.router.navigate([`/candidate-dashboard`]);
          this.utilService.openSnackBar('Skills saved successfully!', 'Dismiss');
        }

      },
      error: (error) => {
        this.loading = false;
        this.checkButtonStatus();
        this.utilService.openSnackBar('Failed to save skills!', 'Retry');
      },
    });
  }
}
