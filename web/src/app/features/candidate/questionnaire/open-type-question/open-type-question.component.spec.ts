import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenTypeQuestionComponent } from './open-type-question.component';

describe('OpenTypeQuestionComponent', () => {
  let component: OpenTypeQuestionComponent;
  let fixture: ComponentFixture<OpenTypeQuestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OpenTypeQuestionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenTypeQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
