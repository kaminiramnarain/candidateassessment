import { Component, OnInit, ViewChild } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import * as moment from 'moment';
import { ReviewService } from 'src/app/shared/service/api/review/review.service';


@Component({
  selector: 'app-review-dashboard',
  templateUrl: './review-dashboard.component.html',
  styleUrls: ['./review-dashboard.component.scss']
})
export class ReviewDashboardComponent implements OnInit {

  constructor(private reviewService: ReviewService,) { }

  public totalElements!: number;
  public pageSizeOptions: number[] = [];
  public candidates: any[] = [];
  public dataSource!: any;
  public searchValue!: string;
  public personId!: string;

  displayedColumns: string[] = [
    'userQuestionnaire.firstName',
    'userQuestionnaire.emailAddress',
    'userQuestionnaire.interviewDate',
    'userQuestionnaire.questionnaire.templateName',
    'userQuestionnaire.timeTakenToCompleteQuestionnaire',
    'userQuestionnaire.marks',
    'actions',
  ];

  public sortOrder: string = 'ASC';
  public sortBy: string = 'userQuestionnaire.firstName';
  public pageNumber: any = 0;
  public pageSize: any = 10;
  public pageEvent!: PageEvent;
  public searchEventValue!: string;
  @ViewChild(MatSort) sort!: MatSort;


  ngOnInit(): void {
    this.getAllUserQuestionnaires();
  }

  public onKey(event: any): void {
    if (event.target !== undefined) {
      this.searchEventValue = event.target.value;
    }
    this.reviewService
      .searchByCandidateName(
        this.searchEventValue,
        this.sortOrder,
        this.sortBy,
        this.pageNumber,
        this.pageSize,
        this.personId,
      )
      .subscribe((data) => {
        this.candidates = data.content;
        this.candidates.forEach((candidate) => {
          candidate.timeTakenToCompleteQuestionnaire=(candidate.timeTakenToCompleteQuestionnaire/60).toFixed(0);
          candidate.interviewDate= (moment(candidate.interviewDate)).format('MMMM Do YYYY');
        });
        this.dataSource = new MatTableDataSource(this.candidates);
        this.dataSource.sort = this.sort;
        this.totalElements = data.totalElements;
      });
  }

  public sortData(event: any) {

    this.sortOrder = event.direction;
    this.sortBy = event.active;
    this.sortOrder = this.sortOrder.toUpperCase();

    if (this.searchValue === '' || this.searchValue === undefined) {
      this.getAllUserQuestionnaires();
    } else {
      this.onKey(this.searchEventValue);
    }
  }

  public changePageData(event: PageEvent) {
    this.pageNumber = event.pageIndex;
    this.pageSize = event.pageSize;
    if (this.searchValue === '' || this.searchValue === undefined) {
      this.getAllUserQuestionnaires();
    } else {
      this.onKey(this.searchEventValue);
    }
    return event;
  }


  public resetSearchValue(): void {
    this.searchValue = '';
    this.getAllUserQuestionnaires();
  }

  public getAllUserQuestionnaires(): void {
    const personId = localStorage.getItem('personId');

    if (personId != undefined) {
      this.personId = personId;
    }

    this.reviewService
      .getUserQuestionnaireData(
        this.personId,
        this.sortOrder,
        this.sortBy,
        this.pageNumber,
        this.pageSize
      )
      .subscribe((data) => {
        this.candidates = data.content;
        this.candidates.forEach((candidate) => {
          candidate.timeTakenToCompleteQuestionnaire=(candidate.timeTakenToCompleteQuestionnaire/60).toFixed(0);
          candidate.interviewDate= (moment(candidate.interviewDate)).format('MMMM Do YYYY');
        });
        this.dataSource = new MatTableDataSource(this.candidates);
        this.totalElements = data.totalElements;
        this.dataSource.sort = this.sort;
      });
  }

}
