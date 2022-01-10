import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MultipleChoiceQuestionEditComponent } from './multiple-choice-question-edit.component';

describe('MultipleChoiceQuestionEditComponent', () => {
  let component: MultipleChoiceQuestionEditComponent;
  let fixture: ComponentFixture<MultipleChoiceQuestionEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MultipleChoiceQuestionEditComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MultipleChoiceQuestionEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
