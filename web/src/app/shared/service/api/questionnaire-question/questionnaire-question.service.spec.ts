import { TestBed } from '@angular/core/testing';

import { QuestionnaireQuestionService } from './questionnaire-question.service';

describe('QuestionnaireQuestionService', () => {
  let service: QuestionnaireQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuestionnaireQuestionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
