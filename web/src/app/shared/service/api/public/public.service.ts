import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ValidateTokenDto, SkillDto , UserQuestionnaireSkillDto, QuestionDto, Questionnaire, SubmitAnswerDto} from 'src/app/shared/model';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class PublicService {

  constructor(private http: HttpClient) { }

  public validateToken(token: string): Observable<ValidateTokenDto> {
    const url = `${environment.apiUrl}/public/validateToken/${token}`;
    return this.http.get<ValidateTokenDto>(url, httpOptions);
  }

  public validateId(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/public/validateId/${userQuestionnaireId}`;
    return this.http.get<any>(url, httpOptions);
  }

  public getMarks(userQuestionnaireId: string): Observable<number> {
    const url = `${environment.apiUrl}/public/marks/${userQuestionnaireId}`;
    return this.http.get<number>(url, httpOptions);
  }

  public getQuestionnaireType(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/public/getQuestionnaireType/${userQuestionnaireId}`;
    return this.http.get<any>(url, httpOptions);
  }
  
  public getSkills(name?: string): Observable<SkillDto[]> {
    const url = `${environment.apiUrl}/public/skills`;
    let params = new HttpParams();
    if (name) {
      params = params.append('skillName', name!);
    }
    return this.http.get<SkillDto[]>(url, { params: params });
  }

  public submitSkills(body: UserQuestionnaireSkillDto): Observable<UserQuestionnaireSkillDto> {
    const url = `${environment.apiUrl}/public/user-questionnaire-skills`;
    return this.http.post<UserQuestionnaireSkillDto>(url, body, environment.httpOptions);
  }

  
  public getCandidateSelectSkillsStatus(userQuestionnaireId: string): Observable<ValidateTokenDto> {
    const url = `${environment.apiUrl}/public/getCandidateSelectSkillsStatus/${userQuestionnaireId}`;
    return this.http.get<ValidateTokenDto>(url, httpOptions);
  }

  public generateQuestionnaire(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/public/generate?userQuestionnaireId=${userQuestionnaireId}`;
    return this.http.post<any>(url, environment.httpOptions);
  }

  public getQuestion(userQuestionnaireId: string, questionNumber: number): Observable<QuestionDto> {
    const url = `${environment.apiUrl}/public/question`;
    let params = new HttpParams();
    params = params.append('userQuestionnaireId', userQuestionnaireId);
    params = params.append('questionNumber', questionNumber);
    return this.http.get<QuestionDto>(url, {
      params: params, headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    });
  }

  public updateCheatCount(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/public/updateCheatCount/${userQuestionnaireId}`;
    return this.http.put<any>(url, httpOptions);
  }

  public getCheatCount(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/public/getCheatCount/${userQuestionnaireId}`;
    return this.http.get<any>(url, httpOptions);
  }

  public disqualifyQuestionnaire(userQuestionnaireId: string, remainingTime: number): Observable<any> {
    const url = `${environment.apiUrl}/public/disqualifyQuestionnaire/${userQuestionnaireId}?remainingTime=${remainingTime}`;
    return this.http.put<any>(url, httpOptions);
  }

  public getQuestionnaireData(userQuestionnaireId: string): Observable<Questionnaire> {
    const url = `${environment.apiUrl}/questionnaires/getData/${userQuestionnaireId}`;
    return this.http.get<Questionnaire>(url, environment.httpOptions);
  }

  public submitAnswer(answer: SubmitAnswerDto): Observable<SubmitAnswerDto> {
    const url = `${environment.apiUrl}/public/saveAnswer`;
    return this.http.post<SubmitAnswerDto>(url, answer, environment.httpOptions);
  }

  
  public finishQuestionnaire(userQuestionnaireId: string, remainingTime: number): Observable<any> {
    const url = `${environment.apiUrl}/public/finish?userQuestionnaireId=${userQuestionnaireId}&remainingTime=${remainingTime}`;
    return this.http.put<any>(url, environment.httpOptions);
  }

  
  public updateRemainingTime(userQuestionnaireId: string, remainingTime: number): Observable<any> {
    var body = {
      "userQuestionnaireId": userQuestionnaireId,
      "remainingTime": remainingTime
    }
    const url = `${environment.apiUrl}/public/updateRemainingTime`;
    return this.http.put<any>(url, body);
  }

}
