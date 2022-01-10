import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { QuestionDto, QuestionTypeEnum, SkillDto, SkillLevelEnum, ViewQuestionDto } from 'src/app/shared/model';
import { QuestionService } from 'src/app/shared/service/api/question/question.service';
import { SkillService } from 'src/app/shared/service/api/skill/skill.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';
import { DeleteQuestionDialogComponent } from './delete-question-dialog/delete-question-dialog.component';
import { ViewQuestionDialogComponent } from './view-question-dialog/view-question-dialog.component';

@Component({
  selector: 'app-questions-dashboard',
  templateUrl: './questions-dashboard.component.html',
  styleUrls: ['./questions-dashboard.component.scss']
})
export class QuestionsDashboardComponent implements OnInit {
  public totalElements!: number;
  public pageSizeOptions: number[] = [];
  public questions: ViewQuestionDto[] = [];
  public searchValue!: string;
  public dataSource!: any;
  public searchValueSkill!: string;
  public skillList!: SkillDto[];
  public selectedSkillId!: string;
  public skillLevelList: any = Object.keys(SkillLevelEnum).map(skillLevel =>
    skillLevel = this.utilsService.titleCase(skillLevel.replace("_", " "))
  ).filter(String);
  public selectedSkillLevel!: string;
  public searchSkillLevel!: string;
  public questionTypeList: any = Object.keys(QuestionTypeEnum).map(questionType =>
    questionType = this.utilsService.titleCase(questionType.replace("_", " "))
  ).filter(String);
  public selectedQuestionType!: string;
  public searchQuestionType!: string;
  displayedColumns: string[] = [
    'questionEnglish',
    'questionType',
    'skill',
    'level',
    'marks',
    'time',
    'actions',
  ];
  public sortOrder: string = 'ASC';
  public sortBy: string = 'questionEnglish';
  public pageNumber: any = 0;
  public pageSize: any = 10;
  public pageEvent!: PageEvent;
  public searchEventValue!: string;

  public previousSearchValue!: string;
  public previousQuestionType!: string;
  public previousSkill!: string;
  public previousLevel!: string;

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private questionService: QuestionService,
    public dialog: MatDialog,
    private utilsService: UtilsService,
    private skillService: SkillService
  ) { }

  ngOnInit(): void {
    this.getAllQuestions();
  }

  public getAllQuestions(): void {
    this.questionService
      .getQuestions(
        this.sortOrder,
        this.sortBy,
        this.pageNumber,
        this.pageSize
      )
      .subscribe((questions) => {
        this.questions = questions.content;
        this.dataSource = new MatTableDataSource(this.questions);
        this.totalElements = questions.totalElements;
        this.dataSource.sort = this.sort;
      });
    this.skillService.getSkills('').subscribe((skill) => {
      this.skillList = skill;
    })
  }

  public getSkills(): void{
    this.skillService.getSkills('').subscribe((skill) => {
      this.skillList = skill;
    })
  }

  public onClose(): void {
    this.searchValueSkill = "";
    this.getSkills();
  }

  public searchQuestions(goToFirstPage: boolean): void {
    this.questionService
      .searchQuestions(
        this.searchEventValue,
        this.selectedSkillId,
        this.searchSkillLevel,
        this.searchQuestionType,
        this.sortOrder,
        this.sortBy,
        this.pageNumber,
        this.pageSize
      )
      .subscribe((questions) => {
        this.questions = questions.content;
        this.dataSource = new MatTableDataSource(this.questions);
        this.dataSource.sort = this.sort;
        this.totalElements = questions.totalElements;
      });

    if (goToFirstPage) {
      this.paginator.firstPage();
    }
  }

  public sortData(event: any) {
    this.sortOrder = event.direction;
    this.sortBy = event.active;

    if (this.searchValue === '' || this.searchValue === undefined) {
      this.getAllQuestions();
    } else {
      this.onKey(this.searchEventValue);
    }
  }

  public changePageData(event: PageEvent) {
    this.pageNumber = event.pageIndex;
    this.pageSize = event.pageSize;

    this.searchQuestions(false);

  }

  public onKey(event: any): void {
    if (event.target !== undefined) {
      this.searchEventValue = event.target.value;
    }
    this.searchQuestions(true);
  }

  public resetSearchValue(): void {
    this.searchEventValue = '';
    this.getAllQuestions();
  }

  public skillOnKey(event: any): void {
    this.searchValueSkill = event.target.value;
    this.skillService
      .getSkills(this.searchValueSkill)
      .subscribe((skill) => {
        this.skillList.length = 0;
        skill.forEach((skillResult) => {
          this.skillList.push(skillResult);
        });
      });
  }

  public resetSearchFields(): void {
    this.searchEventValue = '';
    this.selectedSkillId = '';
    this.selectedSkillLevel = '';
    this.searchSkillLevel = '';
    this.selectedQuestionType = '';
    this.searchQuestionType = '';
    this.getAllQuestions();
  }

  public selectionChanged(changeField: string, event: any): void {

    if (changeField == 'skill') {
      this.selectedSkillId = event.value;
    }

    if (changeField == 'level') {
      this.searchSkillLevel = event.value.toUpperCase();
    }

    if (changeField == 'questionType') {
      this.searchQuestionType = event.value.replace(" ", "_").toUpperCase();
    }

    this.searchQuestions(true);

  }

  public deleteQuestion(questionId: string): void {
    this.questions = this.questions.filter(
      (question) => question.id !== questionId
    );
    this.questionService
      .deleteQuestion(questionId)
      .subscribe({
        next: () => {
          (this.questions = this.questions.filter(
            (question) => question.id !== questionId
          ))
          this.dataSource = new MatTableDataSource(this.questions);
          this.dataSource.sort = this.sort;
          this.totalElements = this.questions.length;
          this.utilsService.openSnackBar("Question deleted successfully!", "Dismiss");
        },
        error: (error) => {
          this.utilsService.openSnackBar("Failed to delete question!", "Retry");
        }
      });

  }

  public convertTextToReadable(text: string): string {
    return this.utilsService.titleCase(text);
  }

  public openDialog(elementId: string): void {
    const dialogRef = this.dialog.open(DeleteQuestionDialogComponent, {});

    dialogRef.afterClosed().subscribe((result) => {
      if (result) this.deleteQuestion(elementId);
    });
  }

  public viewQuestion(question: ViewQuestionDto): void {

    const dialogRef = this.dialog.open(ViewQuestionDialogComponent, {
      width: '92vw',
      height: '80vh',
      backdropClass: "backdrop",
      data: question,
      autoFocus: false
    });

    dialogRef.afterClosed().subscribe((result) => {
    });

  }

}
