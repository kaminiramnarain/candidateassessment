import { AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { MultipleAnswerDto } from 'src/app/shared/model';
import { PublicService } from 'src/app/shared/service/api/public/public.service';
import { QuestionNumberService } from 'src/app/shared/service/question-number/question-number.service';

@Component({
  selector: 'app-multiple-answers-question-questionnaire',
  templateUrl: './multiple-answers-question.component.html',
  styleUrls: ['./multiple-answers-question.component.scss']
})
export class MultipleAnswersQuestionComponent implements OnInit, OnDestroy, AfterViewInit {

  @Input() answers: MultipleAnswerDto[] = [];
  @Input() selectedQuestion!: number;
  @Output() onChangeMultipleAnswersQuestion: EventEmitter<{}> = new EventEmitter();
  public isClearSelectionButtonDisabled: boolean = true;
  public subscription!: Subscription;

  constructor(private route: ActivatedRoute, private publicService: PublicService, private questionNumberService: QuestionNumberService) {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.subscription = this.questionNumberService.getQuestionNumberChangeEmitter().subscribe((questionNumber) => {
      console.log("received multiple answers" + questionNumber);

      this.selectedQuestion = questionNumber;
      this.publicService.getQuestion(this.route.snapshot.params['id'], this.selectedQuestion).subscribe((question) => {
        this.answers = question.multipleAnswer;
        this.isClearSelectionButtonDisabled = true;
        if (this.answers != null) {
          this.answers.forEach(answer => {
            if (answer.value == true) this.isClearSelectionButtonDisabled = false;
          });
        }
      });
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public trackByIndex(index: number, obj: any): any {
    return index;
  }

  public appendChange(event: any, id: string): void {
    this.answers.forEach(answer => {
      if (answer.id == id) answer.value = event.checked;
    });
    this.isClearSelectionButtonDisabled = true;
    this.answers.forEach(answer => {
      if (answer.value == true) this.isClearSelectionButtonDisabled = false;
    });
    this.onChangeMultipleAnswersQuestion.emit(this.answers);
  }

  public resetAnswers(): void {
    this.answers.forEach(answer => answer.value = false);
    this.isClearSelectionButtonDisabled = true;
    this.onChangeMultipleAnswersQuestion.emit(this.answers);
  }

}
