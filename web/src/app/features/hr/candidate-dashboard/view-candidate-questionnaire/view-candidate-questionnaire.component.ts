import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FilledQuestionnaireDto, QuestionTypeEnum } from 'src/app/shared/model';
import { QuestionnaireService } from 'src/app/shared/service/api/questionnaire/questionnaire.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';

@Component({
  selector: 'app-view-candidate-questionnaire',
  templateUrl: './view-candidate-questionnaire.component.html',
  styleUrls: ['./view-candidate-questionnaire.component.scss']
})
export class ViewCandidateQuestionnaireComponent implements OnInit {

  public questionnaire!: FilledQuestionnaireDto;

  constructor(private route: ActivatedRoute, private questionnaireService: QuestionnaireService, private utilsService: UtilsService) { }

  ngOnInit(): void {
    this.initialiseQuestionnaire();
    this.questionnaireService.getQuestionnaire(this.route.snapshot.params['id']).subscribe((questionnaire) => {
      questionnaire.timeTaken = Math.round(questionnaire.timeTaken / 60);
      this.questionnaire = questionnaire;
    });
  }

  private initialiseQuestionnaire(): void {
    this.questionnaire = {
      questions: [],
      firstName: '',
      lastName: ',',
      marks: 0,
      marksObtained: 0,
      length: 0,
      timeTaken: 0
    }
  }

  public isQuestionOpenEnded(questionType: QuestionTypeEnum): boolean {
    return questionType == QuestionTypeEnum.OPEN_ENDED;
  }

  public isCheckIcon(valid: boolean, selected: boolean): boolean {

    if (valid && selected) {
      return true;
    } else if (!valid && !selected) {
      return true
    } else {
      return false;
    }
  }

  public titleCase(text: any): string {
    return this.utilsService.titleCase(text.toString().replace("_", " "));
  }

}
