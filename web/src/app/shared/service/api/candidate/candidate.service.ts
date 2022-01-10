import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CandidateDto, CandidatesWhoAreNotAssignedInterviewDateDto, CandidateReviewDto } from 'src/app/shared/model';
import { Page } from 'src/app/shared/model';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root',
})
export class CandidateService {
  constructor(private http: HttpClient) { }

  public getCandidates(sortOrder: string, sortBy: string, pageNumber: number, pageSize: number): Observable<Page<CandidateDto[]>> {
    const url = `${environment.apiUrl}/candidates?sortOrder=${sortOrder}&sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.http.get<Page<CandidateDto[]>>(url, httpOptions);
  }

  public getCandidatesForCustomizedQuestionnaires(personId: string, sortOrder: string, sortBy: string, pageNumber: number, pageSize: number): Observable<Page<CandidateDto[]>> {
    const url = `${environment.apiUrl}/candidates/customizeQuestionnaire/${personId}?sortOrder=${sortOrder}&sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.http.get<Page<CandidateDto[]>>(url, httpOptions);
  }

  public searchCandidatesForCustomizedQuestionnaires(personId: string, candidateName: string, sortOrder: string, sortBy: string, pageNumber: number, pageSize: number): Observable<Page<CandidateDto[]>> {
    const url = `${environment.apiUrl}/candidates/customizeQuestionnaire/${personId}/search?candidateName=${candidateName}&sortOrder=${sortOrder}&sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.http.get<Page<CandidateDto[]>>(url, httpOptions);
  }

  public searchCandidates(searchInput: string, sortOrder: string, sortBy: string, pageNumber: number, pageSize: number): Observable<Page<CandidateDto[]>> {
    const url = `${environment.apiUrl}/candidates/search?candidateName=${searchInput}&sortOrder=${sortOrder}&sortBy=${sortBy}&pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.http.get<Page<CandidateDto[]>>(url, httpOptions);
  }

  public deleteCandidate(candidateId: string): Observable<CandidateDto[]> {
    const url = `${environment.apiUrl}/candidates/archive/${candidateId}`;
    return this.http.put<CandidateDto[]>(url, httpOptions);
  }

  public getCandidateById(candidateId: string): Observable<CandidateDto> {
    const url = `${environment.apiUrl}/candidates/${candidateId}`;
    return this.http.get<CandidateDto>(url, httpOptions);
  }

  public getCandidatesWhoAreNotAssignedInterviewDate(): Observable<CandidatesWhoAreNotAssignedInterviewDateDto[]> {
    const url = `${environment.apiUrl}/candidates/noInterviewDate`;
    return this.http.get<CandidatesWhoAreNotAssignedInterviewDateDto[]>(url, httpOptions);
  }

  public getCandidateDataForReview(candidateId: string): Observable<CandidateReviewDto> {
    const url = `${environment.apiUrl}/candidates/forReview/${candidateId}`;
    return this.http.get<CandidateReviewDto>(url, httpOptions);
  }

  public searchCandidatesWhoAreNotAssignedInterviewDateByName(searchInput: string): Observable<CandidatesWhoAreNotAssignedInterviewDateDto[]> {
    const url = `${environment.apiUrl}/candidates/noInterviewDate/search?candidateName=${searchInput}`;
    return this.http.get<CandidatesWhoAreNotAssignedInterviewDateDto[]>(url, httpOptions);
  }


}
