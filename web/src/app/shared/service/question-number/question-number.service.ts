import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class QuestionNumberService {

  private questionNumberChange: EventEmitter<number> = new EventEmitter();

  constructor() { }

  public emitQuestionNumberChangeEvent(event: number): void {
    this.questionNumberChange.emit(event);
  }

  public getQuestionNumberChangeEmitter(): EventEmitter<number> {
    return this.questionNumberChange;
  }

}
