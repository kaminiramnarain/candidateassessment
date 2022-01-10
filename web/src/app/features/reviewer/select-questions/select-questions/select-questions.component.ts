import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { ActivatedRoute, Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import {
  CandidateDto, PositionDto, ReviewerDto,
  SelectedSkillDto, QuestionnaireStatusEnum, QuestionTypeEnum, SkillDto, SkillLevelEnum, ViewQuestionDto
} from 'src/app/shared/model';
import { QuestionService } from 'src/app/shared/service/api/question/question.service';
import { SkillService } from 'src/app/shared/service/api/skill/skill.service';
import { QuestionnaireService } from 'src/app/shared/service/api/questionnaire/questionnaire.service';
import { CandidateService } from 'src/app/shared/service/api/candidate/candidate.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';
import { ViewQuestionDialogComponent } from 'src/app/features/hr/questions-dashboard/view-question-dialog/view-question-dialog.component';
import { UserQuestionnaireService } from 'src/app/shared/service/api/user-questionnaire/user-questionnaire.service';

@Component({
  selector: 'app-select-questions',
  templateUrl: './select-questions.component.html',
  styleUrls: ['./select-questions.component.scss']
})
export class SelectQuestionsComponent implements OnInit {

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
    'skill',
    'level',
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
  public candidate!: CandidateDto;
  public positionDto!: PositionDto;
  public selectedSkill!: SelectedSkillDto[];
  public assignedReviewer!: ReviewerDto[];
  public selectedQuestions: ViewQuestionDto[] = [];
  public questionnaireLength!: number;
  public totalTime: number = 0;
  public totalMarks: number = 0;
  public disabled: boolean = false;
  public timeToCompleteQuestionnaire: any = 0;
  public idList: string[] = [];

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private questionService: QuestionService,
    public dialog: MatDialog,
    private router: Router,
    private utilsService: UtilsService,
    private skillService: SkillService,
    private questionnaireService: QuestionnaireService,
    private candidateService: CandidateService,
    private route: ActivatedRoute,
    private userQuestionnaireService: UserQuestionnaireService) { }

  ngOnInit(): void {
    this.checkId();
    this.initialiseCandidate();
    this.getCandidate();
    this.getAllQuestions();
  }

  private getCandidate(): void {
    this.candidateService
      .getCandidateById(this.route.snapshot.params['id'])
      .subscribe((candidate) => {
        this.candidate = candidate;
        this.questionnaireLength = candidate.remainingTime;
      });

  }

  public checkId(): void {
    this.userQuestionnaireService.validateId(this.route.snapshot.params['id']).subscribe({
      next: () => {
      },
      error: (error) => {
        this.router.navigate(['/page-not-found']);
      },
    });
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
        if (this.selectedQuestions.length !== 0) {
          this.selectedQuestions.forEach(selectedQuestion => {
            questions.content = questions.content.filter(question => question.id !== selectedQuestion.id);
            questions.totalElements = questions.totalElements - 1;
          })
        }
        this.questions = questions.content;
        this.dataSource = new MatTableDataSource(this.questions);
        this.totalElements = questions.totalElements;
        this.dataSource.sort = this.sort;
        if (this.questions.length === 0) {
          this.paginator.nextPage();
        }
      });
    this.skillService.getSkills('').subscribe((skill) => {
      this.skillList = skill;
    })
  }

  public getSkills(): void {
    this.skillService.getSkills('').subscribe((skill) => {
      this.skillList = skill;
    })
  }

  public convertTextToReadable(text: string): string {
    return this.utilsService.titleCase(text);
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
        if (this.selectedQuestions.length !== 0) {
          this.selectedQuestions.forEach(selectedQuestion => {
            this.questions = this.questions.filter(question => question.id !== selectedQuestion.id);
            questions.totalElements = questions.totalElements - 1;
          })
        }
        this.dataSource = new MatTableDataSource(this.questions);
        this.dataSource.sort = this.sort;
        this.totalElements = questions.totalElements;
      });
    console.log(this.questions.length);

    if (this.questions.length === 1) {
      this.paginator.nextPage();
    }
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

  public addToQuestionnaire(selectedQuestion: ViewQuestionDto): void {
    if (this.questionnaireLength > this.totalTime) {
      this.selectedQuestions.push(selectedQuestion);
      let marks = 0;
      this.selectedQuestions.forEach(question => marks = question.marks + marks);
      this.totalMarks = marks;
      let time = 0;
      this.selectedQuestions.forEach(question => time = question.time + time);
      this.totalTime = time;
      this.timeToCompleteQuestionnaire = (this.totalTime / 60).toFixed(0);
      if (this.searchEventValue != undefined ||
        this.selectedSkillId != undefined ||
        this.selectedSkillLevel != undefined ||
        this.searchSkillLevel != undefined ||
        this.selectedQuestionType != undefined ||
        this.searchQuestionType != undefined) {
        this.searchQuestions(true);
      } else {
        this.getAllQuestions();
      }
    } else {
      this.disabled = true;
      this.utilsService.openSnackBar('Cannot add more questions!', 'Create the questionnaire');
    }
  }


  public removeFromQuestionnaire(selectedQuestion: ViewQuestionDto): void {
    this.selectedQuestions = this.selectedQuestions.filter(question => question.id !== selectedQuestion.id);
    let marks = 0;
    this.selectedQuestions.forEach(question => marks = question.marks + marks);
    this.totalMarks = marks;
    let time = 0;
    this.selectedQuestions.forEach(question => time = question.time + time);
    this.totalTime = time;
    this.timeToCompleteQuestionnaire = (this.totalTime / 60).toFixed(0);
    if (this.searchEventValue != undefined ||
      this.selectedSkillId != undefined ||
      this.selectedSkillLevel != undefined ||
      this.searchSkillLevel != undefined ||
      this.selectedQuestionType != undefined ||
      this.searchQuestionType != undefined) {
      this.searchQuestions(true);
    } else {
      this.getAllQuestions();
    }
  }

  public onSubmit(): void {
    if (this.totalTime < this.questionnaireLength) {
      this.utilsService.openSnackBar('Add more questions to create questionnaire!', 'Dismiss');
    }
    this.selectedQuestions.forEach(question => this.idList.push(question.id));

    this.questionnaireService
      .customizeQuestionnaire(this.route.snapshot.params['id'], this.idList)
      .subscribe(()=>{
      this.router.navigate([`/customize-questionnaire-dashboard`]);
      });
  }

  private initialiseCandidate(): void {
    this.positionDto = {
      id: '',
      name: '',
    };

    this.assignedReviewer = [
      {
        id: '',
        firstName: '',
        lastName: '',
      },
    ];

    this.selectedSkill = [
      {
        id: '',
        name: '',
        level: SkillLevelEnum.NOVICE,
      },
    ];

    this.candidate = {
      id: '',
      remainingTime: 0,
      firstName: '',
      lastName: '',
      phoneNumber: '',
      emailAddress: '',
      position: this.positionDto,
      userArchived: false,
      questionnaireStatus: QuestionnaireStatusEnum.QUESTIONNAIRE_NOT_GENERATED,
      skills: this.selectedSkill,
      reviewers: this.assignedReviewer,
    };
  }

}
