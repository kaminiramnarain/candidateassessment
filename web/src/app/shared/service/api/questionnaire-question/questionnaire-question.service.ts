import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {QuestionnaireDataForReviewDto} from 'src/app/shared/model';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class QuestionnaireQuestionService {

  constructor(private http: HttpClient) { }


public getQuestionsForReview(userQuestionnaireId: string): Observable<QuestionnaireDataForReviewDto[]> {
  const url = `${environment.apiUrl}/questionnaire-questions/getDataForReview/${userQuestionnaireId}`;
  return this.http.get<QuestionnaireDataForReviewDto[]>(url, httpOptions);
}

}