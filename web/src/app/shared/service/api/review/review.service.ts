import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CreateReviewDto, CandidateReviewDto } from 'src/app/shared/model';
import { environment } from 'src/environments/environment';
import { Page } from 'src/app/shared/model';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient) { }

  public createReview(body: CreateReviewDto): Observable<CreateReviewDto> {
    const url = `${environment.apiUrl}/reviews`;
    return this.http.post<CreateReviewDto>(
      url,
      body,
      httpOptions
    );
  }

  public getUserQuestionnaireData(personId: string, sortOrder: string, sortBy: string, pageNumber: number, pageSize: number): Observable<Page<CandidateReviewDto[]>> {
    const url = `${environment.apiUrl}/reviews/getUserQuestionnaireData/${personId}?sortOrder=${sortOrder}&sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.http.get<Page<CandidateReviewDto[]>>(url, httpOptions);
  }

  public searchByCandidateName(candidateName: string, sortOrder: string, sortBy: string, pageNumber: number, pageSize: number, personId: string): Observable<Page<CandidateReviewDto[]>> {
    const url = `${environment.apiUrl}/reviews/getUserQuestionnaireData/${personId}/search?candidateName=${candidateName}&sortOrder=${sortOrder}&sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.http.get<Page<CandidateReviewDto[]>>(url, httpOptions);
  }
}
