import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SkillDto, UserQuestionnaireSkillDto } from 'src/app/shared/model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})

export class SkillService {
  constructor(private http: HttpClient) { }

  public getSkills(name?: string): Observable<SkillDto[]> {
    const url = `${environment.apiUrl}/skills`;
    let params = new HttpParams();
    if (name) {
      params = params.append('skillName', name!);
    }
    return this.http.get<SkillDto[]>(url, { params: params });
  }

  public submitSkills(body: UserQuestionnaireSkillDto): Observable<UserQuestionnaireSkillDto> {
    const url = `${environment.apiUrl}/user-questionnaire-skills`;
    return this.http.post<UserQuestionnaireSkillDto>(url, body, environment.httpOptions);
  }
}
