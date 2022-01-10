import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MarksDialogComponent } from 'src/app/features/candidate/marks-dialog/marks-dialog.component';
import { QuestionTypeEnum, ViewQuestionDto } from 'src/app/shared/model';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';

@Component({
  selector: 'app-view-question-dialog',
  templateUrl: './view-question-dialog.component.html',
  styleUrls: ['./view-question-dialog.component.scss']
})
export class ViewQuestionDialogComponent implements OnInit {

  public displayQuestionType!: string;
  public displayLevel!: string;
  public showAnswerSection: boolean = false;
  constructor(
    public dialogRef: MatDialogRef<MarksDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ViewQuestionDto, private utilsService: UtilsService) { }

  ngOnInit(): void {
    this.displayQuestionType = this.utilsService.titleCase(this.data.questionType);
    this.displayLevel = this.utilsService.titleCase(this.data.level);
    if (this.data.questionType != QuestionTypeEnum.OPEN_ENDED) {
      this.showAnswerSection = true;
    }

  }

}
