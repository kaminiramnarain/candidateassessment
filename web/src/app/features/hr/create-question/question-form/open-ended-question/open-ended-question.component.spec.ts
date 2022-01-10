import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenEndedQuestionComponent } from './open-ended-question.component';

describe('OpenEndedQuestionComponent', () => {
  let component: OpenEndedQuestionComponent;
  let fixture: ComponentFixture<OpenEndedQuestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OpenEndedQuestionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenEndedQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
