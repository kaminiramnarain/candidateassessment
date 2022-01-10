import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SubmitAnswerDto, SaveCandidateMarksDto } from 'src/app/shared/model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})

export class CandidateAnswerService {

  constructor(private http: HttpClient) { }

  public submitAnswer(answer: SubmitAnswerDto): Observable<SubmitAnswerDto> {
    const url = `${environment.apiUrl}/candidate-answers`;
    return this.http.post<SubmitAnswerDto>(url, answer, environment.httpOptions);
  }

  public saveCandidateMarks(saveCandidateMarksDto: SaveCandidateMarksDto): Observable<SaveCandidateMarksDto> {
    const url = `${environment.apiUrl}/candidate-answers/saveMarks`;
    return this.http.put<SaveCandidateMarksDto>(url, saveCandidateMarksDto, environment.httpOptions);
  }

  public finishQuestionnaire(userQuestionnaireId: string, remainingTime: number): Observable<any> {
    const url = `${environment.apiUrl}/candidate-answers/finish?userQuestionnaireId=${userQuestionnaireId}&remainingTime=${remainingTime}`;
    return this.http.put<any>(url, environment.httpOptions);
  }


}
