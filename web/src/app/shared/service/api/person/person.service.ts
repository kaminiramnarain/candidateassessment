import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ReviewerDto } from 'src/app/shared/model';
import { environment } from 'src/environments/environment';


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class PersonService {

  constructor(private http: HttpClient) { }

  public getReviewers(): Observable<ReviewerDto[]> {
    const url = `${environment.apiUrl}/persons/reviewers`;
    return this.http.get<ReviewerDto[]>(url, httpOptions);
  }

  public getReviewersByName(reviewerName: string): Observable<ReviewerDto[]> {
    const url = `${environment.apiUrl}/persons/reviewers/search?reviewerName=${reviewerName}`;
    return this.http.get<ReviewerDto[]>(url, httpOptions);
  }


}
