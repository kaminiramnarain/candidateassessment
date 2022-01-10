import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomizeQuestionnaireDashboardComponent } from './customize-questionnaire-dashboard.component';

describe('CustomizeQuestionnaireDashboardComponent', () => {
  let component: CustomizeQuestionnaireDashboardComponent;
  let fixture: ComponentFixture<CustomizeQuestionnaireDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomizeQuestionnaireDashboardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomizeQuestionnaireDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
