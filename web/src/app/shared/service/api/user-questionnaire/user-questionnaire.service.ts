import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateUserQuestionnaireDto, UpdateUserQuestionnaireDto, UserQuestionnaireDto, ValidateTokenDto, InviteCandidateDto, SaveReviewedUserQuestionnaireDto, FilledQuestionnaireDto } from 'src/app/shared/model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root',
})
export class UserQuestionnaireService {
  constructor(private http: HttpClient) { }


  public createUserQuestionnaire(
    body: CreateUserQuestionnaireDto
  ): Observable<UserQuestionnaireDto> {
    const url = `${environment.apiUrl}/user-questionnaires`;
    return this.http.post<UserQuestionnaireDto>(
      url,
      body,
      httpOptions
    );
  }

  public updateUserQuestionnaire(userQuestionnaireId: string, body: UpdateUserQuestionnaireDto): Observable<UpdateUserQuestionnaireDto> {
    const url = `${environment.apiUrl}/user-questionnaires/update/${userQuestionnaireId}`;
    return this.http.put<UpdateUserQuestionnaireDto>(
      url,
      body,
      httpOptions
    );
  }



  public saveReviewedUserQuestionnaire(body: SaveReviewedUserQuestionnaireDto, userQuestionnaireId: string): Observable<SaveReviewedUserQuestionnaireDto> {
    const url = `${environment.apiUrl}/user-questionnaires/saveReviewedUserQuestionnaire/${userQuestionnaireId}`;
    return this.http.put<SaveReviewedUserQuestionnaireDto>(
      url,
      body,
      httpOptions
    );
  }


  public updateRemainingTime(userQuestionnaireId: string, remainingTime: number): Observable<any> {
    var body = {
      "userQuestionnaireId": userQuestionnaireId,
      "remainingTime": remainingTime
    }
    const url = `${environment.apiUrl}/user-questionnaires/updateRemainingTime`;
    return this.http.put<any>(url, body);
  }

  public updateCheatCount(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/user-questionnaires/updateCheatCount/${userQuestionnaireId}`;
    return this.http.put<any>(url, httpOptions);
  }

  public getCheatCount(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/user-questionnaires/getCheatCount/${userQuestionnaireId}`;
    return this.http.get<any>(url, httpOptions);
  }

  
  public disqualifyQuestionnaire(userQuestionnaireId: string, remainingTime: number): Observable<any> {
    const url = `${environment.apiUrl}/user-questionnaires/disqualifyQuestionnaire/${userQuestionnaireId}?remainingTime=${remainingTime}`;
    return this.http.put<any>(url, httpOptions);
  }



  public inviteCandidate(body: InviteCandidateDto, userQuestionnaireId: string): Observable<InviteCandidateDto> {
    const url = `${environment.apiUrl}/user-questionnaires/inviteCandidate/${userQuestionnaireId}`;
    return this.http.put<InviteCandidateDto>(
      url,
      body,
      httpOptions
    );
  }


  public validateToken(token: string): Observable<ValidateTokenDto> {
    const url = `${environment.apiUrl}/user-questionnaires/validateToken/${token}`;
    return this.http.get<ValidateTokenDto>(url, httpOptions);
  }

  
  public getQuestionnaireType(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/user-questionnaires/getQuestionnaireType/${userQuestionnaireId}`;
    return this.http.get<any>(url, httpOptions);
  }
  
  public validateId(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/user-questionnaires/validateId/${userQuestionnaireId}`;
    return this.http.get<any>(url, httpOptions);
  }

  public validateIdAndUnderReview(userQuestionnaireId: string): Observable<any> {
    const url = `${environment.apiUrl}/user-questionnaires/validateIdAndUnderReview/${userQuestionnaireId}`;
    return this.http.get<any>(url, httpOptions);
  }

  public getCandidateSelectSkillsStatus(userQuestionnaireId: string): Observable<ValidateTokenDto> {
    const url = `${environment.apiUrl}/user-questionnaires/getCandidateSelectSkillsStatus/${userQuestionnaireId}`;
    return this.http.get<ValidateTokenDto>(url, httpOptions);
  }


  public getMarks(userQuestionnaireId: string): Observable<number> {
    const url = `${environment.apiUrl}/user-questionnaires/marks/${userQuestionnaireId}`;
    return this.http.get<number>(url, httpOptions);
  }

}
