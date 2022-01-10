import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateCandidateProfileComponent } from './update-candidate-profile.component';

describe('UpdateCandidateProfileComponent', () => {
  let component: UpdateCandidateProfileComponent;
  let fixture: ComponentFixture<UpdateCandidateProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateCandidateProfileComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateCandidateProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
