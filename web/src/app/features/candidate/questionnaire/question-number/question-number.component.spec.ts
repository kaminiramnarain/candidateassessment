import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionNumberComponent } from './question-number.component';

describe('QuestionNumberComponent', () => {
  let component: QuestionNumberComponent;
  let fixture: ComponentFixture<QuestionNumberComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QuestionNumberComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QuestionNumberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
