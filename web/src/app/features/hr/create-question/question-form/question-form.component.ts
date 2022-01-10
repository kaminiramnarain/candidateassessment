import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NewQuestion, QuestionTypeEnum } from 'src/app/shared/model';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';

@Component({
  selector: 'app-question-form',
  templateUrl: './question-form.component.html',
  styleUrls: ['./question-form.component.scss']
})
export class QuestionFormComponent implements OnInit {
  @Input() question!: NewQuestion;
  @Input() hasDeleteButton: boolean = false;
  @Input() expanded: boolean = false;
  @Output() onRemoveQuestionEmitter: EventEmitter<string> = new EventEmitter();
  public QuestionTypeEnum = QuestionTypeEnum;
  public selectedQuestionType!: string;
  @Input() questionNumber!: number;
  public questionTypes: any = Object.keys(QuestionTypeEnum).map(questionType =>
    questionType = this.utilsService.titleCase(questionType.replace("_", " "))
  ).filter(String);

  constructor(private utilsService: UtilsService) { }

  ngOnInit(): void {
  }

  removeQuestion(): void {
    this.onRemoveQuestionEmitter.emit(this.question.id);
  }

  public checkQuestionType(questionType: QuestionTypeEnum): boolean {
    if (this.selectedQuestionType == this.utilsService.titleCase(questionType.replace("_", " "))) return true;
    else return false;
  }


}
