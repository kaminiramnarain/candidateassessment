import { Component, OnInit, ChangeDetectorRef, OnDestroy, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { CheatDialogComponent } from './cheat-dialog/cheat-dialog/cheat-dialog.component';
import { IsAnsweredQuestionDto, QuestionDto, QuestionTypeEnum, SubmitAnswerDto } from 'src/app/shared/model';
import { PublicService } from 'src/app/shared/service/api/public/public.service';
import { QuestionNumberService } from 'src/app/shared/service/question-number/question-number.service';

@Component({
  selector: 'app-questionnaire',
  templateUrl: './questionnaire.component.html',
  styleUrls: ['./questionnaire.component.scss'],
})
export class QuestionnaireComponent implements OnInit, OnDestroy {
  public QuestionTypeEnum = QuestionTypeEnum;
  public isButtonSubmit: boolean = false;
  public isTimeFinishing: boolean = false;
  public selectedQuestion: number = 1;
  public timeLeft: string = "";
  public remainingTime!: number;
  public displayRemainingTime!: string;
  public duration!: number;
  public numberOfQuestions!: number;
  public questionsNumbers: IsAnsweredQuestionDto[] = [];
  public question!: QuestionDto;
  public answerDto!: SubmitAnswerDto;
  public ticker!: any;
  public userQuestionnaireId!: string;
  public cheatCount!: number;
  public updateDbInterval: number = 15;

  constructor(public dialog: MatDialog, private route: ActivatedRoute, private cdr: ChangeDetectorRef, private publicService: PublicService, private router: Router, private questionNumberService: QuestionNumberService) {
    if (!(this.route.snapshot.params['id'] === undefined))
      this.userQuestionnaireId = this.route.snapshot.params['id'];

    window.addEventListener('blur', () => {
    });
    window.addEventListener('focus', (e) => {
      this.checkCheatCount();
    });

  }

  public checkCheatCount(): void {
  if(this.router.url.includes("questionnaire")){
    this.publicService.updateCheatCount(this.userQuestionnaireId).subscribe(() => {
      this.publicService.getCheatCount(this.userQuestionnaireId).subscribe((cheat) => {
        this.cheatCount = cheat;
        console.log(cheat)
        this.openDialog();
        if (cheat > 3) {
         this.publicService.disqualifyQuestionnaire(this.userQuestionnaireId, this.remainingTime).subscribe();
          this.router.navigate([`/test-completed/${this.route.snapshot.params['id']}`]);
        }
      });
    });
  }
}

  public openDialog(): void {
    const dialogRef = this.dialog.open(CheatDialogComponent, {
      width: '60vw',
      height: '22vw',
      data: { cheat: this.cheatCount },
    });

    dialogRef.afterClosed().subscribe((result) => {
    });
  }

  ngOnInit(): void {
    this.checkId();
    this.initialiseQuestion();
    this.publicService.getQuestion(this.userQuestionnaireId, this.selectedQuestion).subscribe((question) => {
      this.question = question;
      this.questionNumberService.emitQuestionNumberChangeEvent(this.selectedQuestion);
    });
    this.publicService.getQuestionnaireData(this.userQuestionnaireId).subscribe((questionnaire) => {
      if (!questionnaire.questionnaireOpen) {
        this.router.navigate([`/test-completed/${this.route.snapshot.params['id']}`]);
      }
      this.duration = questionnaire.duration / 60;
      this.remainingTime = questionnaire.remainingTime;
      this.checkTime();
      this.ticker = setInterval(() => {
        this.updateTime();
        this.updateDisplayTime();
        if (this.remainingTime == 0) {
          clearInterval(this.ticker);
          this.submitAnswer(true);
        }
      }, 1000);
      this.numberOfQuestions = questionnaire.questions.length;
      this.initialiseQuestionnaire(questionnaire);
    });

  }

  public checkId(): void {
    this.publicService.validateId(this.userQuestionnaireId).subscribe({
      next: () => {
      },
      error: (error) => {
        this.router.navigate(['/page-not-found']);
      },
    });
  }

  ngOnDestroy(): void {
    clearInterval(this.ticker);
  }

  ngAfterViewChecked(): void {
    this.cdr.detectChanges();
  }

  private checkTime(): void {
    if (this.remainingTime == 0) {
      clearInterval(this.ticker);
      this.router.navigate([`/test-completed/${this.route.snapshot.params['id']}`]);
    }
    this.isTimeFinishing = this.remainingTime <= 180 ? true : false;
    this.updateDisplayTime();
  }

  private initialiseQuestionnaire(questionnaire: any): void {
    this.questionsNumbers = [];
    for (var i = 0; i < questionnaire.questions.length; i++) {
      this.questionsNumbers.push(new IsAnsweredQuestionDto(i + 1, questionnaire.questions[i].answered));
    }
  }

  private initialiseQuestion(): void {
    this.question = {
      answerText: '',
      id: '',
      marks: 0,
      multipleAnswer: [],
      possibleAnswers: [],
      questionEnglish: '',
      questionFrench: '',
      questionNumber: 0,
      type: QuestionTypeEnum.MULTIPLE_CHOICE
    };
  }

  public submit(): void {
    if (this.isButtonSubmit) {
      this.submitAnswer(true)
    } else {
      this.selectedQuestion++;
      this.changeQuestion(this.selectedQuestion);
    }
  }

  private submitAnswer(isSubmit: boolean): void {
    this.answerDto = {
      userQuestionnaireId: this.userQuestionnaireId,
      questionId: this.question.id,
      questionType: this.question.type,
      textAnswer: this.question.type == QuestionTypeEnum.OPEN_ENDED || this.QuestionTypeEnum.MULTIPLE_CHOICE ? this.question.answerText! : null!,
      multipleAnswers: this.question.type == QuestionTypeEnum.MULTIPLE_ANSWERS ? this.question.multipleAnswer! : null!
    }
    this.publicService.submitAnswer(this.answerDto).subscribe(() => {
      if (!isSubmit) {
        this.publicService.getQuestion(this.userQuestionnaireId, this.selectedQuestion).subscribe((question) => {
          this.question = question;
          this.publicService.getQuestionnaireData(this.userQuestionnaireId).subscribe((questionnaire) => {
            this.initialiseQuestionnaire(questionnaire);
            this.questionNumberService.emitQuestionNumberChangeEvent(this.selectedQuestion);
            this.checkButtonStatus();
          });

        });
      } else {
        this.publicService.finishQuestionnaire(this.route.snapshot.params['id'], this.remainingTime).subscribe();
        this.router.navigate([`/test-completed/${this.route.snapshot.params['id']}`]);
      }
    });
  }

  public changeQuestion(questionNumber: any): void {
    this.selectedQuestion = questionNumber;
    this.submitAnswer(false);


  }

  private checkButtonStatus(): void {
    if (this.selectedQuestion == this.numberOfQuestions)
      this.isButtonSubmit = true;
    else this.isButtonSubmit = false;
  }

  public checkQuestionType(questionType: QuestionTypeEnum): boolean {
    if (this.question.type == questionType) return true;
    else return false;
  }

  public updateAnswer(event: any, questionId: string): void {
    if (this.question.type == QuestionTypeEnum.OPEN_ENDED || this.question.type == QuestionTypeEnum.MULTIPLE_CHOICE) {
      this.question.answerText = event;
    } else if (this.question.type == QuestionTypeEnum.MULTIPLE_ANSWERS) {
      this.question.multipleAnswer = event;
    }
  }

  private updateTime(): void {
    this.remainingTime = this.remainingTime - 1;
    if (this.remainingTime % this.updateDbInterval == 0) {
      this.publicService.updateRemainingTime(this.userQuestionnaireId, this.remainingTime).subscribe();
    }
    this.isTimeFinishing = this.remainingTime <= 180 ? true : false;
  }

  public updateDisplayTime(): void {
    var hours = Math.floor(this.remainingTime / 3600);
    var minutes = Math.floor(this.remainingTime % 3600 / 60);
    var seconds = Math.floor(this.remainingTime % 3600 % 60);

    minutes = minutes + (60 * hours);

    this.displayRemainingTime = minutes.toLocaleString('en-US', {
      minimumIntegerDigits: 2,
      useGrouping: false
    }) + ":" + seconds.toLocaleString('en-US', {
      minimumIntegerDigits: 2,
      useGrouping: false
    });
  }
}
