import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginDto } from 'src/app/shared/model';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  // public login(emailAddress: string, password: string): Observable<LoginDto> {
  //   const url = `${environment.apiUrl}/persons/validateCredentials`;
  //   var body = {
  //     "emailAddress": emailAddress,
  //     "password": password
  //   };
  //   return this.http.post<LoginDto>(url, body, httpOptions);
  // }


}
