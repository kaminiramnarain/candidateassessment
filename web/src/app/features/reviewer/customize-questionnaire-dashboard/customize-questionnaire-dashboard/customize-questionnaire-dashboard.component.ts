import { Component, OnInit, ViewChild } from '@angular/core';
import { CandidateDto} from 'src/app/shared/model';
import { CandidateService } from 'src/app/shared/service/api/candidate/candidate.service';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';


@Component({
  selector: 'app-customize-questionnaire-dashboard',
  templateUrl: './customize-questionnaire-dashboard.component.html',
  styleUrls: ['./customize-questionnaire-dashboard.component.scss']
})
export class CustomizeQuestionnaireDashboardComponent implements OnInit {

  public totalElements!: number;
  public pageSizeOptions: number[] = [];
  public candidates: CandidateDto[] = [];
  public searchValue!: string;
  public dataSource!: any;

  displayedColumns: string[] = [
    'userQuestionnaire.firstName',
    'userQuestionnaire.emailAddress',
    'userQuestionnaire.position.name',
    'skill',
    'actions',
  ];
  public sortOrder: string = 'ASC';
  public sortBy: string = 'userQuestionnaire.firstName';
  public pageNumber: any = 0;
  public pageSize: any = 10;
  public pageEvent!: PageEvent;
  public searchEventValue!: string;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  public personId!: string;

  constructor(
    private candidateService: CandidateService,
    public dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.getAllCandidates();
  }

  public getAllCandidates(): void {
    const personId = localStorage.getItem('personId');

    if (personId != undefined) {
      this.personId = personId;
    }

    this.candidateService
      .getCandidatesForCustomizedQuestionnaires(
        this.personId,
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
    this.sortOrder = this.sortOrder.toUpperCase();

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
      .searchCandidatesForCustomizedQuestionnaires(
        this.personId,
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


}
