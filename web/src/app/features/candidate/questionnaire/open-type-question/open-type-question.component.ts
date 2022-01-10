import { AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit, Output, SimpleChange, SimpleChanges } from '@angular/core';
import { QuestionDto, QuestionTypeEnum } from 'src/app/shared/model';
import { PublicService } from 'src/app/shared/service/api/public/public.service';
import Quill from 'quill'
import BlotFormatter from 'quill-blot-formatter';
import { QuestionNumberService } from 'src/app/shared/service/question-number/question-number.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

Quill.register('modules/blotFormatter', BlotFormatter);

@Component({
  selector: 'app-open-type-question-questionnaire',
  templateUrl: './open-type-question.component.html',
  styleUrls: ['./open-type-question.component.scss']
})
export class OpenTypeQuestionComponent implements OnInit, OnDestroy, AfterViewInit {
  @Output() onChangeOpenTypeQuestion: EventEmitter<string> = new EventEmitter();
  @Input() selectedQuestion!: number;
  public question!: QuestionDto;
  public subscription!: Subscription;

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

  constructor(private route: ActivatedRoute, private publicService: PublicService, private questionNumberService: QuestionNumberService) { }

  ngOnInit(): void {
    this.initialiseQuestion();
  }

  ngAfterViewInit(): void {
    this.questionNumberService.getQuestionNumberChangeEmitter().subscribe((questionNumber) => {
      console.log("received open ended" + questionNumber);

      this.selectedQuestion = questionNumber;
      this.publicService.getQuestion(this.route.snapshot.params['id'], questionNumber).subscribe((question) => {
        this.question = question;
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

  public onTyping(event: any): void {
    if (event.event === 'text-change')
      this.onChangeOpenTypeQuestion.emit(event.html);
  }

}
