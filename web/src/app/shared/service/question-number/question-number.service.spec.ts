import { TestBed } from '@angular/core/testing';

import { QuestionNumberService } from './question-number.service';

describe('QuestionNumberService', () => {
  let service: QuestionNumberService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuestionNumberService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
