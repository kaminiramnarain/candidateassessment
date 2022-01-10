import { TestBed } from '@angular/core/testing';

import { CandidateAnswerService } from './candidate-answer.service';

describe('CandidateAnswerService', () => {
  let service: CandidateAnswerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CandidateAnswerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
