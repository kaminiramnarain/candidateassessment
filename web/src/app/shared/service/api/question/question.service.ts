import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DashboardDto, EditQuestionDto, Page, QuestionDto, SkillLevelEnum, ViewQuestionDto } from 'src/app/shared/model';
import { NewQuestion } from 'src/app/shared/model';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  constructor(private http: HttpClient) { }

  public getQuestion(userQuestionnaireId: string, questionNumber: number): Observable<QuestionDto> {
    const url = `${environment.apiUrl}/questions`;
    let params = new HttpParams();
    params = params.append('userQuestionnaireId', userQuestionnaireId);
    params = params.append('questionNumber', questionNumber);
    return this.http.get<QuestionDto>(url, {
      params: params, headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  }

  public validateId(questionId: string): Observable<any> {
    const url = `${environment.apiUrl}/questions/validateId/${questionId}`;
    return this.http.get<any>(url, httpOptions);
  }

  public getQuestionById(questionId: string): Observable<EditQuestionDto> {
    const url = `${environment.apiUrl}/questions/${questionId}`;
    return this.http.get<EditQuestionDto>(url);
  }

  public updateQuestion(questionId: string, body: any): Observable<EditQuestionDto> {
    const url = `${environment.apiUrl}/questions/${questionId}`;
    return this.http.put<EditQuestionDto>(url, body);
  }

  public getQuestions(sortOrder: string, sortBy: string, pageNumber: number, pageSize: number): Observable<Page<ViewQuestionDto[]>> {
    const url = `${environment.apiUrl}/questions/all?sortOrder=${sortOrder}&sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.http.get<Page<ViewQuestionDto[]>>(url, environment.httpOptions);
  }

  public searchQuestions(searchInput: string, selectedSkillId: string, selectedSkillLevel: string, selectedQuestionType: string, sortOrder: string, sortBy: string, pageNumber: number, pageSize: number): Observable<Page<ViewQuestionDto[]>> {
    var url = `${environment.apiUrl}/questions/search?sortOrder=${sortOrder}&sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
    if (searchInput != undefined && searchInput != '' && searchInput != null) {
      url += "&questionContent=" + searchInput;
    }
    if (selectedSkillId != undefined && selectedSkillId != '' && selectedSkillId != null) {
      url += "&selectedSkillId=" + selectedSkillId;
    }
    if (selectedSkillLevel != undefined && selectedSkillLevel != '' && selectedSkillLevel != null) {
      url += "&selectedSkillLevel=" + selectedSkillLevel;
    }
    if (selectedQuestionType != undefined && selectedQuestionType != '' && selectedQuestionType != null) {
      url += "&selectedQuestionType=" + selectedQuestionType;
    }
    return this.http.get<Page<ViewQuestionDto[]>>(url, environment.httpOptions);
  }

  public deleteQuestion(questionId: string): Observable<ViewQuestionDto[]> {
    const url = `${environment.apiUrl}/questions/${questionId}`;
    return this.http.delete<ViewQuestionDto[]>(url, environment.httpOptions);
  }


  public saveQuestion(body: any): Observable<NewQuestion[]> {
    const url = `${environment.apiUrl}/questions`;
    return this.http.post<NewQuestion[]>(url, body);
  }

  public getDashboardData(): Observable<DashboardDto[]> {
    const url = `${environment.apiUrl}/questions/getDashboardData`;
    return this.http.get<DashboardDto[]>(url, httpOptions);
  }

}
