import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MultipleAnswersQuestionEditComponent } from './multiple-answers-question-edit.component';

describe('MultipleAnswersQuestionEditComponent', () => {
  let component: MultipleAnswersQuestionEditComponent;
  let fixture: ComponentFixture<MultipleAnswersQuestionEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MultipleAnswersQuestionEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MultipleAnswersQuestionEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
