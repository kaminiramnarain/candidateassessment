import { AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import {
  CandidateDto,
  PositionDto,
  QuestionnaireStatusEnum,
  ReviewerDto,
  SelectedSkillDto,
  SkillLevelEnum,
  UpdateUserQuestionnaireDto,
} from 'src/app/shared/model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CandidateService } from 'src/app/shared/service/api/candidate/candidate.service';
import { PositionService } from 'src/app/shared/service/api/position/position.service';
import { UserQuestionnaireService } from 'src/app/shared/service/api/user-questionnaire/user-questionnaire.service';
import { ChangeDetectionStrategy } from '@angular/compiler/src/compiler_facade_interface';


@Component({
  selector: 'app-update-candidate-profile',
  templateUrl: './update-candidate-profile.component.html',
  styleUrls: ['./update-candidate-profile.component.scss']
})
export class UpdateCandidateProfileComponent implements OnInit, AfterViewChecked {

  public updateUserQuestionnaireDto!: UpdateUserQuestionnaireDto;
  public candidateId!: string;
  public allPositions: PositionDto[] = [];
  public searchValuePosition!: string;
  public candidate!: CandidateDto;
  public positionDto!: PositionDto;
  public selectedSkill!: SelectedSkillDto[];
  public assignedReviewer!: ReviewerDto[];


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
  });

  constructor(private route: ActivatedRoute,
    private candidateService: CandidateService,
    private positionService: PositionService,
    private userQuestionnaireService: UserQuestionnaireService,
    private router: Router,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
  ) {
    this.candidateId = this.route.snapshot.params['id'];
  }

  ngOnInit(): void {
    this.checkId();
    this.initializeCandidate();
    this.getPositions();
    this.getCandidate();
  }

  ngAfterViewChecked(): void {
    this.cdr.detectChanges();
  }

  public onClosePosition(): void {
    this.searchValuePosition = "";
    this.getPositions();
  }


  public checkId(): void {
    this.userQuestionnaireService.validateId(this.candidateId).subscribe({
      next: () => {
      },
      error: (error) => {
        this.router.navigate(['/page-not-found']);
      },
    });
  }

  public onSubmit(): void {
    this.updateUserQuestionnaireDto = {
      firstName: this.candidate.firstName,
      lastName: this.candidate.lastName,
      phoneNumber: this.candidate.phoneNumber,
      emailAddress: this.candidate.emailAddress,
      positionId: this.candidate.position.id,
    };

    this.userQuestionnaireService
      .updateUserQuestionnaire(this.candidateId, this.updateUserQuestionnaireDto)
      .subscribe({
        next: () => {
          this.router.navigate(['/candidate-dashboard']);
          this.openSnackBar('Candidate profile successfully updated!', 'Dismiss');
        },
        error: (error) => {
          this.openSnackBar('Failed to update candidate profile!', 'Retry');
        },
      });
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

  private getCandidate(): void {
    this.candidateService
      .getCandidateById(this.candidateId)
      .subscribe((candidate) => {
        this.candidate = candidate;
      });
  }

  private openSnackBar(message: string, action: string): void {
    this.snackBar.open(message, action, {
      duration: 3000,
    });
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
      remainingTime:0,
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
