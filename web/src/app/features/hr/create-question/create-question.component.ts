import { Component, OnInit, ViewChild } from '@angular/core';
import { MatAccordion } from '@angular/material/expansion';
import { Router } from '@angular/router';
import { NewQuestion, QuestionTypeEnum } from 'src/app/shared/model';
import { QuestionService } from 'src/app/shared/service/api/question/question.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';
import { v4 as uuid } from 'uuid';

@Component({
  selector: 'app-create-question',
  templateUrl: './create-question.component.html',
  styleUrls: ['./create-question.component.scss']
})
export class CreateQuestionComponent implements OnInit {
  @ViewChild(MatAccordion) accordion!: MatAccordion;

  public questions: NewQuestion[] = [];
  public isSubmitButtonDisabled: boolean = false;

  constructor(private utilsService: UtilsService, private questionService: QuestionService, private router: Router) { }

  ngOnInit(): void {
    this.questions.push(new NewQuestion(uuid()));
  }

  addQuestion(): void {
    var valid = true;
    this.questions.forEach(question => {
      if (question.level == undefined || question.marks == undefined || question.questionType == undefined || question.skillId == undefined || question.questionEnglish == undefined || question.time == undefined) {
        valid = false;
      } else if (question.questionType != QuestionTypeEnum.OPEN_ENDED) {
        question.multipleAnswers.forEach(answer => {
          if (!answer.answerEnglish) valid = false;
        });
      }
    })
    if (valid) {

      var hasAtLeastOneTrue = false;
      var hasMultipleAnswersQuestion = false;

      this.questions.forEach(question => {
        if (question.questionType == QuestionTypeEnum.MULTIPLE_ANSWERS) {
          hasMultipleAnswersQuestion = true;
          question.multipleAnswers.forEach(answer => {
            if (answer.value) hasAtLeastOneTrue = true;
          })
        }
      })

      if (hasMultipleAnswersQuestion) {
        if (hasAtLeastOneTrue) {
          this.questions.push(new NewQuestion(uuid()));
        } else {
          this.utilsService.openSnackBar("Multiple answers question should have atleast one right answer!", "Ok");
        }
      } else {
        this.questions.push(new NewQuestion(uuid()));
      }
    } else {
      this.utilsService.openSnackBar("Fill in all fields before adding a new question!", "Ok");
    }
  }

  removeQuestion(event: any): void {
    this.questions = this.questions.filter(question => question.id != event);
  }

  public submit(): void {

    var valid = true;
    this.questions.forEach(question => {
      if (question.questionEnglish == undefined || question.skillId == undefined || question.level == undefined || question.marks == undefined || question.time == undefined) {
        valid = false;
      }
      if (question.questionType == QuestionTypeEnum.MULTIPLE_ANSWERS || question.questionType == QuestionTypeEnum.MULTIPLE_CHOICE) {
        question.multipleAnswers.forEach(answer => {
          if (answer.answerEnglish == undefined) {
            valid = false;
          }
        })
      }
    });

    if (valid) {
      var body = {
        "questions": this.questions
      };
      this.questionService.saveQuestion(body).subscribe({
        next: () => {
          this.utilsService.openSnackBar("Question(s) saved successfully!", "Ok");
          this.router.navigate(['/questions-dashboard']);
        },
        error: (error) => {
          this.utilsService.openSnackBar("Failed to save question(s). Please check network!", "Ok");
        },
      });

    } else {
      this.utilsService.openSnackBar("Please fill in all fields before submitting!", "Ok");
    }

  }

  public cancel(): void {
    this.router.navigate(['/questions-dashboard']);
  }

}
