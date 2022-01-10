import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PositionDto } from 'src/app/shared/model';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  constructor(private http: HttpClient) { }

  public getPositions(): Observable<PositionDto[]> {
    const url = `${environment.apiUrl}/positions`;
    return this.http.get<PositionDto[]>(url, httpOptions);
  }

  public getPositionsByName(positionName: string): Observable<PositionDto[]> {
    const url = `${environment.apiUrl}/positions/search?positionName=${positionName}`;
    return this.http.get<PositionDto[]>(url, httpOptions);
  }
}
