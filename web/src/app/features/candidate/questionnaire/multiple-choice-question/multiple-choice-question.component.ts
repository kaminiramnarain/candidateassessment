import { AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { AnswerDto, QuestionDto, QuestionTypeEnum } from 'src/app/shared/model';
import { QuestionService } from 'src/app/shared/service/api/question/question.service';
import { QuestionNumberService } from 'src/app/shared/service/question-number/question-number.service';

@Component({
  selector: 'app-multiple-choice-question-questionnaire',
  templateUrl: './multiple-choice-question.component.html',
  styleUrls: ['./multiple-choice-question.component.scss']
})
export class MultipleChoiceQuestionComponent implements OnInit, OnDestroy, AfterViewInit {
  @Input() selectedQuestion!: number;
  @Input() possibleAnswers!: AnswerDto[];
  @Output() onChangeMultipleChoiceQuestion: EventEmitter<string> = new EventEmitter();
  public question!: QuestionDto;
  public answer: string = '';
  public isClearSelectionButtonDisabled: boolean = true;
  private subscription!: Subscription;

  constructor(private route: ActivatedRoute, private questionService: QuestionService, private questionNumberService: QuestionNumberService) { }

  ngOnInit(): void {
    this.initialiseQuestion();
  }

  ngAfterViewInit(): void {
    this.subscription = this.questionNumberService.getQuestionNumberChangeEmitter().subscribe((questionNumber) => {
      console.log("received multiple choice" + questionNumber);


      this.selectedQuestion = questionNumber;
      this.questionService.getQuestion(this.route.snapshot.params['id'], this.selectedQuestion).subscribe((question) => {
        this.question = question;
        this.answer = question.answerText;
        this.isClearSelectionButtonDisabled = this.answer == null || this.answer == '' ? true : false;
      });
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
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
      type: QuestionTypeEnum.OPEN_ENDED
    };
  }

  public clearSelection(): void {
    this.answer = '';
    this.isClearSelectionButtonDisabled = true;
    this.onChangeMultipleChoiceQuestion.emit(this.answer);
  }

  public changeSelection(): void {
    this.isClearSelectionButtonDisabled = this.answer == null || this.answer == '' ? true : false;
    this.onChangeMultipleChoiceQuestion.emit(this.answer);
  }

}
