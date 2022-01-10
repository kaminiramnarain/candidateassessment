import { TestBed } from '@angular/core/testing';

import { UserQuestionnaireService } from './user-questionnaire.service';

describe('UserQuestionnaireService', () => {
  let service: UserQuestionnaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserQuestionnaireService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
