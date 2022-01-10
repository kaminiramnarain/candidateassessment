import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenEndedQuestionEditComponent } from './open-ended-question-edit.component';

describe('OpenEndedQuestionEditComponent', () => {
  let component: OpenEndedQuestionEditComponent;
  let fixture: ComponentFixture<OpenEndedQuestionEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OpenEndedQuestionEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenEndedQuestionEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
