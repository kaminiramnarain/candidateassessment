import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CandidateReviewDto, QuestionnaireDto, QuestionnaireDataForReviewDto, SaveCandidateMarksDto, SaveReviewedUserQuestionnaireDto } from 'src/app/shared/model';
import { CandidateService } from 'src/app/shared/service/api/candidate/candidate.service';
import { UserQuestionnaireService } from 'src/app/shared/service/api/user-questionnaire/user-questionnaire.service';
import { QuestionnaireQuestionService } from 'src/app/shared/service/api/questionnaire-question/questionnaire-question.service';
import { CandidateAnswerService } from 'src/app/shared/service/api/candidate-answer/candidate-answer.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';
import * as moment from 'moment';
import { CdkTextareaAutosize } from '@angular/cdk/text-field';

@Component({
  selector: 'app-review-answers',
  templateUrl: './review-answers.component.html',
  styleUrls: ['./review-answers.component.scss']
})
export class ReviewAnswersComponent implements OnInit {

  public userQuestionnaireId!: string;
  public candidate!: CandidateReviewDto;
  public timeTakenToCompleteQuestionnaire!: any;
  public interviewDate: Date = new Date;
  public questionnaire!: QuestionnaireDto;
  public interviewDateString!: string;
  public questionnaireDataList: QuestionnaireDataForReviewDto[] = [];
  public marked: boolean = false;
  public saveCandidateMarksDtos: SaveCandidateMarksDto[] = [];
  public saveCandidateMarksDto!: SaveCandidateMarksDto;
  public comment!: string;
  public totalMarks!: number;
  public saveReviewedUserQuestionnaireDto!: SaveReviewedUserQuestionnaireDto;


  public reviewForm = new FormGroup({
    reviewComment: new FormControl()
  });

  constructor(private route: ActivatedRoute, private utilsService: UtilsService, private userQuestionnaireService: UserQuestionnaireService, private candidateAnswerService: CandidateAnswerService, private candidateService: CandidateService, private questionnaireQuestionService: QuestionnaireQuestionService, private router: Router) {
    if (!(this.route.snapshot.params['id'] === undefined))
      this.userQuestionnaireId = this.route.snapshot.params['id'];
  }

  ngOnInit(): void {
    this.checkId();
    this.initializeCandidate();
    this.getCandidateDetails(this.userQuestionnaireId);
    this.getReviewQuestions(this.userQuestionnaireId);
  }

  public checkId(): void {
    //make check if it is under review
    this.userQuestionnaireService.validateIdAndUnderReview(this.userQuestionnaireId).subscribe({
      next: () => {
      },
      error: (error) => {
        console.log("already reviewed")
        this.router.navigate(['/page-not-found']);
      },
    });
  }

  public initializeCandidate(): void {
    this.questionnaire = {
      id: "",
      marks: 0,
      totalTime: 0,
      templateName: ""
    }
    this.candidate = {
      firstName: "",
      lastName: "",
      emailAddress: "",
      interviewDate: new Date(),
      questionnaire: this.questionnaire,
      timeTakenToCompleteQuestionnaire: 0,
      marks: 0,
      remainingTime: 0
    }
  }

  private getReviewQuestions(userQuestionnaireId: string): void {
    this.questionnaireQuestionService.getQuestionsForReview(userQuestionnaireId).subscribe((data) => {
      data.forEach((data) => this.questionnaireDataList.push(data));
    });

  }

  private getCandidateDetails(userQuestionnaireId: string): void {
    this.candidateService.getCandidateDataForReview(userQuestionnaireId).subscribe((cand) => {
      this.candidate = cand;
      this.totalMarks = cand.marks;
      console.log(this.candidate);
      this.timeTakenToCompleteQuestionnaire = (cand.timeTakenToCompleteQuestionnaire / 60).toFixed(0);
      this.interviewDateString = (moment(cand.interviewDate)).format('MMMM Do YYYY')
    });
  }

  public OnCorrection(data: QuestionnaireDataForReviewDto, correction: string, index: any): void {
    let buttonId;

    if (correction === "correct") {
      this.saveCandidateMarksDto = {
        candidateAnswerId: data.candidateAnswerId,
        marksAllocated: data.question.marks
      }
      buttonId = "incorrect" + index;
    }
    else {
      this.saveCandidateMarksDto = {
        candidateAnswerId: data.candidateAnswerId,
        marksAllocated: 0
      }
      buttonId = "correct" + index;
    }
    if (this.saveCandidateMarksDtos.some(dto => dto.candidateAnswerId === data.candidateAnswerId)) {
      let mark = this.saveCandidateMarksDtos.filter(dto => dto.candidateAnswerId === data.candidateAnswerId).map(dto => dto.marksAllocated);
      if (mark[0] === 0) {
        (document.getElementById("incorrect" + index) as HTMLInputElement).classList.add('checked');
        (document.getElementById("correct" + index) as HTMLInputElement).disabled = true;
        (document.getElementById("incorrect" + index) as HTMLInputElement).disabled = false;
        (document.getElementById("correct" + index) as HTMLInputElement).classList.remove('checked');
      } else {
        (document.getElementById("correct" + index) as HTMLInputElement).classList.add('checked');
        (document.getElementById("incorrect" + index) as HTMLInputElement).disabled = true;
        (document.getElementById("correct" + index) as HTMLInputElement).disabled = false;
        (document.getElementById("incorrect" + index) as HTMLInputElement).classList.remove('checked');
      }
      this.saveCandidateMarksDtos.filter(dto => dto.candidateAnswerId === data.candidateAnswerId).map(dto => dto.marksAllocated = this.saveCandidateMarksDto.marksAllocated);
      this.candidateAnswerService.saveCandidateMarks(this.saveCandidateMarksDto)
        .subscribe({
          next: () => {
          },
          error: (error) => {
            this.utilsService.openSnackBar('Review of question was unsuccessful!', 'Retry');
          },

        });
    }
    else {
      (document.getElementById(buttonId) as HTMLInputElement).classList.add('checked');
      if (correction === "correct") {
        (document.getElementById("correct" + index) as HTMLInputElement).disabled = true;
      }
      else {
        (document.getElementById("incorrect" + index) as HTMLInputElement).disabled = true;
      }
      this.saveCandidateMarksDtos.push(this.saveCandidateMarksDto);
      this.candidateAnswerService.saveCandidateMarks(this.saveCandidateMarksDto)
        .subscribe({
          next: () => {
          },
          error: (error) => {
            this.utilsService.openSnackBar('Review of question was unsuccessful!', 'Retry');
          },

        });
    }
  }

  public onSubmit(): void {

    this.saveCandidateMarksDtos.forEach(dto =>
      this.totalMarks = this.totalMarks + dto.marksAllocated
    );
    this.saveReviewedUserQuestionnaireDto = {
      marks: this.totalMarks,
      comment: this.comment
    }
    this.userQuestionnaireService.saveReviewedUserQuestionnaire(this.saveReviewedUserQuestionnaireDto, this.userQuestionnaireId)
      .subscribe({
        next: () => {
          this.router.navigate(['/review-dashboard']);
          this.utilsService.openSnackBar('Successful review of questionnaire!', 'Dismiss');
        },
        error: (error) => {
          this.utilsService.openSnackBar('Review of questionnaire was unsuccessful!', 'Retry');
        },

      });

  }


}
