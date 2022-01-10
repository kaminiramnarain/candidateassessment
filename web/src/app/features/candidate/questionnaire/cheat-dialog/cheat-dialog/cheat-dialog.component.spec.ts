import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheatDialogComponent } from './cheat-dialog.component';

describe('CheatDialogComponent', () => {
  let component: CheatDialogComponent;
  let fixture: ComponentFixture<CheatDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CheatDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CheatDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
