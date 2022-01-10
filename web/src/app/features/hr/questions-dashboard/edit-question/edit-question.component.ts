import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EditQuestionDto, QuestionTypeEnum } from 'src/app/shared/model';
import { QuestionService } from 'src/app/shared/service/api/question/question.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';

@Component({
  selector: 'app-edit-question',
  templateUrl: './edit-question.component.html',
  styleUrls: ['./edit-question.component.scss']
})
export class EditQuestionComponent implements OnInit {

  public questionId!: string;
  public question!: EditQuestionDto;
  public QuestionTypeEnum = QuestionTypeEnum;
  public displayQuestionType !: string;
  constructor(private route: ActivatedRoute, private questionService: QuestionService, private router: Router, private utilsService: UtilsService) { 
    this.questionId = this.route.snapshot.params['id'];
  }

  ngOnInit(): void {
    this.checkId();
  this.getQuestion();
  }

  public getQuestion(): void{
    this.questionService.getQuestionById(this.questionId).subscribe((question) => {
      this.question = question;
      this.displayQuestionType = this.utilsService.titleCase(this.question.questionType);
    });
  }

  public checkId(): void {
    this.questionService.validateId(this.questionId).subscribe({
      next: () => {
      },
      error: (error) => {
        this.router.navigate(['/page-not-found']);
      },
    });
  }

  public checkQuestionType(questionType: QuestionTypeEnum): boolean {
    if (this.question != undefined) {
      if (this.question.questionType == questionType) return true;
      else return false;
    }
    return false;
  }

  public submit(): void {
    this.questionService.updateQuestion(this.question.id, this.question).subscribe({
      next: () => {
        this.router.navigate(['/questions-dashboard']);
        this.utilsService.openSnackBar("Question updated successfully!", "Ok");
      },
      error: () => this.utilsService.openSnackBar("Failed to update question!", "Retry")
    });
  }

  public cancel(): void {
    this.router.navigate(['/questions-dashboard']);
  }

}
