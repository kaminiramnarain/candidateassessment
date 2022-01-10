import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateQuestionnaireDto, QuestionnaireDto, Questionnaire, FilledQuestionnaireDto } from 'src/app/shared/model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})

export class QuestionnaireService {

  constructor(private http: HttpClient) { }

  public createQuestionnaire(
    body: CreateQuestionnaireDto
  ): Observable<QuestionnaireDto> {
    const url = `${environment.apiUrl}/questionnaires`;
    return this.http.post<QuestionnaireDto>(
      url,
      body,
      httpOptions
    );
  }




  public generateQuestionnaire(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/questionnaires/generate?userQuestionnaireId=${userQuestionnaireId}`;
    return this.http.post<any>(url, environment.httpOptions);
  }

  public customizeQuestionnaire(userQuestionnaireId: string, idList : string[]): Observable<any> {
    console.log("lolo0");
    const url = `${environment.apiUrl}/questionnaires/customizeQuestionnaire/${userQuestionnaireId}`;
    return this.http.post<any>(url, idList ,environment.httpOptions);
  }

  public getQuestionnaire(userQuestionnaireId: string): Observable<FilledQuestionnaireDto> {
    const url = `${environment.apiUrl}/questionnaires/getQuestionnaire/${userQuestionnaireId}`;
    return this.http.get<FilledQuestionnaireDto>(url, httpOptions);
  }

}

