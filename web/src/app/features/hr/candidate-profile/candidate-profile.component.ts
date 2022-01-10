import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {
  CandidateDto,
  PositionDto,
  ReviewerDto,
  SelectedSkillDto,
  CreateUserQuestionnaireDto,
  QuestionnaireStatusEnum,
  CreateReviewDto,
  ReviewStatusEnum,
  CreateQuestionnaireDto,
  SkillLevelEnum
} from 'src/app/shared/model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CandidateService } from 'src/app/shared/service/api/candidate/candidate.service';
import { PositionService } from 'src/app/shared/service/api/position/position.service';
import { PersonService } from 'src/app/shared/service/api/person/person.service';
import { UserQuestionnaireService } from 'src/app/shared/service/api/user-questionnaire/user-questionnaire.service';
import { QuestionnaireService } from 'src/app/shared/service/api/questionnaire/questionnaire.service';
import { ReviewService } from 'src/app/shared/service/api/review/review.service';

@Component({
  selector: 'app-candidate-profile',
  templateUrl: './candidate-profile.component.html',
  styleUrls: ['./candidate-profile.component.scss'],
})
export class CandidateProfileComponent implements OnInit {


  public autoGeneratequestionnaireChecked: boolean = false;
  public questionnaireName!: string;
  public userQuestionnaireId!: string;
  public candidate!: CandidateDto;
  public reviewerIds: string[] = [];
  public createQuestionnaireDto!: CreateQuestionnaireDto;
  public createUserQuestionnaireDto!: CreateUserQuestionnaireDto;
  public createReviewDto!: CreateReviewDto;
  public positionDto!: PositionDto;
  public allPositions: PositionDto[] = [];
  public allReviewers: ReviewerDto[] = [];
  public selectedSkill!: SelectedSkillDto[];
  public assignedReviewer!: ReviewerDto[];
  public candidateId!: string;
  public searchValuePosition!: string;
  public searchValueReviewer!: string;
  public questionnaireLength: number = 60;
  public skillChecked: boolean = false;
  public type: any = "0";


  public candidateForm = new FormGroup({
    firstName: new FormControl(null, [
      Validators.required,
      Validators.pattern('[a-zA-Z ]*'),
    ]),
    lastName: new FormControl(null, [
      Validators.required,
      Validators.pattern('[a-zA-Z ]*'),
    ]),
    emailAddress: new FormControl(null, [
      Validators.required,
      Validators.email,
    ]),
    phoneNumber: new FormControl(null, [
      Validators.required,
      Validators.pattern('^[0-9]{8}$'),
    ]),
    position: new FormControl(null, Validators.required),
    reviewer: new FormControl(null, Validators.required),
    questionnaireName: new FormControl(null, [
      Validators.required,
      Validators.pattern('[a-zA-Z ]*'),
    ])
  });

  constructor(
    private candidateService: CandidateService,
    private personService: PersonService,
    private positionService: PositionService,
    private reviewService: ReviewService,
    private userQuestionnaireService: UserQuestionnaireService,
    private router: Router,
    private snackBar: MatSnackBar,
    private questionnaireService: QuestionnaireService
  ) {
  }

  ngOnInit(): void {
    this.initializeCandidate();
    this.getPositions();
    this.getReviewers();
  }

  private getPositions(): void {
    this.positionService.getPositions().subscribe((positions) => {
      positions.forEach((pos) => this.allPositions.push(pos));
    });
  }

  public positionOnKey(event: any): void {
    this.searchValuePosition = event.target.value;
    this.positionService
      .getPositionsByName(this.searchValuePosition)
      .subscribe((position) => {
        this.allPositions.length = 0;
        position.forEach((pos) => this.allPositions.push(pos));
      });
  }

  public reviewerOnKey(event: any): void {
    this.searchValueReviewer = event.target.value;
    this.personService
      .getReviewersByName(this.searchValueReviewer)
      .subscribe((reviewer) => {
        this.allReviewers.length = 0;
        reviewer.forEach((rev) => {
          this.allReviewers.push(rev);
        });
      });
  }

  private getReviewers(): void {
    this.personService.getReviewers().subscribe((reviewers) => {
      reviewers.forEach((rev) => this.allReviewers.push(rev));
    });
  }

  public submitCandidateProfile(checked: boolean): void {
    this.createQuestionnaireDto = {
      templateName: this.questionnaireName,
      totalTime: this.questionnaireLength * 60
    }
    if(this.type==="0"){
      this.type=true;
    }else{
      this.type=false;
    }
    if(this.skillChecked===true){
      this.type=true;
    }
    this.questionnaireService.createQuestionnaire(this.createQuestionnaireDto)
      .subscribe({
        next: (questionnaire) => {

          this.createUserQuestionnaireDto = {
            firstName: this.candidate.firstName,
            lastName: this.candidate.lastName,
            phoneNumber: this.candidate.phoneNumber,
            emailAddress: this.candidate.emailAddress,
            candidateSelectSkills: this.skillChecked,
            remainingTime: this.questionnaireLength * 60,
            status: QuestionnaireStatusEnum.QUESTIONNAIRE_NOT_GENERATED,
            positionId: this.candidate.position.id,
            questionnaireId: questionnaire.id,
            autoGenerate: this.type
          };
          this.userQuestionnaireService
            .createUserQuestionnaire(this.createUserQuestionnaireDto)
            .subscribe((userQuestionnaire) => {
              this.userQuestionnaireId = userQuestionnaire.id;

              this.createReviewDto = {
                personIds: this.reviewerIds,
                status: ReviewStatusEnum.NEW,
                userQuestionnaireId: this.userQuestionnaireId,
              };
              console.log(this.createReviewDto)
              this.reviewService.createReview(this.createReviewDto).subscribe();
              if (this.skillChecked) {
                this.router.navigate(['/candidate-dashboard']);
              }
              else {
                this.router.navigate([`/select-skills/${this.userQuestionnaireId}`]);
              }
              this.openSnackBar('Candidate profile saved successfully!', 'Dismiss');

            });
        },
        error: (error) => {
          this.openSnackBar('Failed to save candidate profile!', 'Retry');
        },

      });
  }

  private openSnackBar(message: string, action: string): void {
    this.snackBar.open(message, action, {
      duration: 3000,
    });
  }

  
  public onClosePosition(): void {
    this.searchValuePosition = "";
    this.getPositions();
  }

  public onCloseReviewer(): void {
    this.searchValueReviewer = "";
    this.getReviewers();
  }



  public changeReviewer(event: any): void {
    this.reviewerIds = [];
    this.reviewerIds.push(event.value.id);
  }

  private initializeCandidate(): void {
    this.positionDto = {
      id: '',
      name: '',
    };

    this.assignedReviewer = [
      {
        id: '',
        firstName: '',
        lastName: '',
      },
    ];

    this.selectedSkill = [
      {
        id: '',
        name: '',
        level: SkillLevelEnum.NOVICE,
      },
    ];

    this.candidate = {
      id: '',
      remainingTime: 0,
      firstName: '',
      lastName: '',
      phoneNumber: '',
      emailAddress: '',
      position: this.positionDto,
      userArchived: false,
      questionnaireStatus: QuestionnaireStatusEnum.QUESTIONNAIRE_NOT_GENERATED,
      skills: this.selectedSkill,
      reviewers: this.assignedReviewer,
    };
  }
}
