import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-question-number',
  templateUrl: './question-number.component.html',
  styleUrls: ['./question-number.component.scss'],
})
export class QuestionNumberComponent implements OnInit {
  @Input() questionNumber!: number;
  @Input() selectedQuestion!: number;
  @Input() isAnswered!: boolean;
  @Output() onChangeQuestion: EventEmitter<any> = new EventEmitter();
  constructor() { }

  ngOnInit(): void {
  }

  public changeQuestion(): void {
    this.onChangeQuestion.emit(this.questionNumber);
  }
}
