import { Component, OnInit, ViewChild } from '@angular/core';
import { CandidateDto, QuestionnaireStatusEnum } from 'src/app/shared/model';
import { CandidateService } from 'src/app/shared/service/api/candidate/candidate.service';
import { MatDialog } from '@angular/material/dialog';
import { DeleteDialogComponent } from './delete-dialog/delete-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-candidate-dashboard',
  templateUrl: './candidate-dashboard.component.html',
  styleUrls: ['./candidate-dashboard.component.scss'],
})
export class CandidateDashboardComponent implements OnInit {
  public totalElements!: number;
  public pageSizeOptions: number[] = [];
  public candidates: CandidateDto[] = [];
  public searchValue!: string;
  public dataSource!: any;

  displayedColumns: string[] = [
    'firstName',
    'emailAddress',
    'position.name',
    'skill',
    'actions',
  ];
  public sortOrder: string = 'ASC';
  public sortBy: string = 'firstName';
  public pageNumber: any = 0;
  public pageSize: any = 10;
  public pageEvent!: PageEvent;
  public searchEventValue!: string;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private candidateService: CandidateService,
    public dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.getAllCandidates();
  }

  public getAllCandidates(): void {
    this.candidateService
      .getCandidates(
        this.sortOrder,
        this.sortBy,
        this.pageNumber,
        this.pageSize
      )
      .subscribe((candidates) => {
        this.candidates = candidates.content;
        this.dataSource = new MatTableDataSource(this.candidates);
        this.totalElements = candidates.totalElements;
        this.dataSource.sort = this.sort;
      });
   
  }

  public sortData(event: any) {
    this.sortOrder = event.direction;
    this.sortBy = event.active;

    if (this.searchValue === '' || this.searchValue === undefined) {
      this.getAllCandidates();
    } else {
      this.onKey(this.searchEventValue);
    }
  }

  public changePageData(event: PageEvent) {
    this.pageNumber = event.pageIndex;
    this.pageSize = event.pageSize;
    if (this.searchValue === '' || this.searchValue === undefined) {
      this.getAllCandidates();
    } else {
      this.onKey(this.searchEventValue);
    }
    return event;
  }

  public onKey(event: any): void {
    if (event.target !== undefined) {
      this.searchEventValue = event.target.value;
    }
    this.candidateService
      .searchCandidates(
        this.searchEventValue,
        this.sortOrder,
        this.sortBy,
        this.pageNumber,
        this.pageSize
      )
      .subscribe((candidates) => {
        this.candidates = candidates.content;
        this.dataSource = new MatTableDataSource(this.candidates);
        this.dataSource.sort = this.sort;
        this.totalElements = candidates.totalElements;
      });
      this.paginator.firstPage();
  }

  public resetSearchValue(): void {
    this.searchValue = '';
    this.getAllCandidates();
  }

  public deleteCandidate(candidateId: string): void {
    this.candidates = this.candidates.filter(
      (candidate) => candidate.id !== candidateId
    );
    this.candidateService
      .deleteCandidate(candidateId)
      .subscribe({
        next: () => {
          (this.candidates = this.candidates.filter(
            (candidate) => candidate.id !== candidateId
          ))
          this.dataSource = new MatTableDataSource(this.candidates);
          this.dataSource.sort = this.sort;
          this.totalElements = this.candidates.length;
          this.openSnackBar('Candidate profile successfully deleted!', 'Dismiss');
        },
        error: (error) => {
          this.openSnackBar('Failed to delete candidate profile!', 'Retry');

        }
      });
  }

  private openSnackBar(message: string, action: string): void {
    this.snackBar.open(message, action, {
      duration: 3000,
    });
  }

  public openDialog(elementId: string): void {
    const dialogRef = this.dialog.open(DeleteDialogComponent, {});

    dialogRef.afterClosed().subscribe((result) => {
      if (result) this.deleteCandidate(elementId);
    });
  }

  public isQuestionnaireCompleted(status: QuestionnaireStatusEnum): boolean {
    return status == QuestionnaireStatusEnum.COMPLETED;
  }

}
