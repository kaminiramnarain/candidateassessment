import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { UserQuestionnaireService } from 'src/app/shared/service/api/user-questionnaire/user-questionnaire.service';
import { MarksDialogComponent } from '../marks-dialog/marks-dialog.component';

@Component({
  selector: 'app-test-completed',
  templateUrl: './test-completed.component.html',
  styleUrls: ['./test-completed.component.scss'],
})
export class TestCompletedComponent implements OnInit {
  private marks!: number;
  private candidateId!: string;

  constructor(public dialog: MatDialog, private route: ActivatedRoute, private router: Router, private userQuestionnaireService: UserQuestionnaireService) 
  {this.candidateId = this.route.snapshot.params['id']; }

  ngOnInit(): void {
    this.checkId();
    this.userQuestionnaireService.getMarks(this.route.snapshot.params['id']).subscribe((marks) => this.marks = marks);
  }
  
  public checkId(): void {
    this.userQuestionnaireService.validateId(this.candidateId).subscribe({
      next: () => {
      },
      error: (error) => {
        this.router.navigate(['/page-not-found']);
      },
    });
  }

  public openDialog(): void {
    const dialogRef = this.dialog.open(MarksDialogComponent, {
      width: '70vw',
      height: '23vw',
      data: { marks: this.marks },
    });

    dialogRef.afterClosed().subscribe((result) => {
    });
  }

  public navigateToHome(): void {
    this.router.navigate(['/']);

  }
}
