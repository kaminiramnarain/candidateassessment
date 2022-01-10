import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCandidateQuestionnaireComponent } from './view-candidate-questionnaire.component';

describe('ViewCandidateQuestionnaireComponent', () => {
  let component: ViewCandidateQuestionnaireComponent;
  let fixture: ComponentFixture<ViewCandidateQuestionnaireComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewCandidateQuestionnaireComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewCandidateQuestionnaireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
