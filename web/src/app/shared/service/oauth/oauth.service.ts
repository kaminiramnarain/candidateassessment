import { Injectable } from '@angular/core';
import { environment } from "../../../../environments/environment";
import { LocalStorageService } from "angular-2-local-storage";
import * as CryptoJS from 'crypto-js';
import { HttpClient, HttpParams } from "@angular/common/http";
import jwt_decode from "jwt-decode";
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class OauthService {
  public reloaded = false;

  constructor(private storage: LocalStorageService, private http: HttpClient, private router: Router,) { }

  public strRandom(length: number) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
  }

  goToLoginPage() {
    const state = this.strRandom(40);
    const code = this.strRandom(128);
    this.storage.set('state', state);
    this.storage.set('codeVerifier', code);
    const codeVerifierHash = CryptoJS.SHA256(code).toString(CryptoJS.enc.Base64);
    const codeChallenge = codeVerifierHash
      .replace(/=/g, '')
      .replace(/\+/g, '-')
      .replace(/\//g, '_');
    const params = [
      'response_type=code',
      'client_id=' + environment.oauthClientId,
      'code_challenge=' + codeChallenge,
      'code_challenge_method=S256',
      'redirect_uri=' + encodeURIComponent(environment.oauthCallbackUrl),
    ];
    window.location.href = environment.oauthLoginUrl + '?' + params.join('&');
  }

  requestAccessToken(code: string, state: string): void {
    const payload = new HttpParams()
      .append('grant_type', 'authorization_code')
      .append('code', code)
      .append('code_verifier', this.storage.get('codeVerifier'))
      .append('redirect_uri', environment.oauthCallbackUrl)
      .append('client_id', environment.oauthClientId);
    console.log(payload)
    this.http.post(environment.oauthTokenUrl, payload, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).subscribe((response: any) => {
      console.log(response);
      this.storage.set('session_state', state);
      this.storage.set('access_token', response.access_token);
      this.storage.set('refresh_token', response.refresh_token);
      // @ts-ignore
      this.storage.set('role', jwt_decode(this.getAccessToken())['resource_access']['client-project-template']['roles'][0]);
      this.storage.set("isLoggedIn", "true");
      // @ts-ignore
      this.storage.set("name", jwt_decode(this.getAccessToken())['name']);
      // @ts-ignore
      this.storage.set("name", jwt_decode(this.getAccessToken())['email']);
    });


  }

  public getAccessToken(): string {
    return this.storage.get('access_token');
  }

  public getRefreshToken(): string {
    return this.storage.get('refresh_token');
  }


  public hasTokenExpired(): boolean {
    if (this.getAccessToken()) {
      const expiry = (JSON.parse(atob(this.getAccessToken().split('.')[1]))).exp;
      return (Math.floor((new Date).getTime() / 1000)) >= expiry;
    }
    return true;
  }

  public refreshToken(): void {
    if (this.getAccessToken()) {
      const payload = new HttpParams()
        .append('client_id', environment.oauthClientId)
        .append('grant_type', 'refresh_token')
        .append('refresh_token', this.getRefreshToken())

      console.log(payload)
      this.http.post(environment.oauthTokenUrl, payload, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }).subscribe((response: any) => {
        console.log(response);
        this.storage.set('access_token', response.access_token);
        this.storage.set('refresh_token', response.refresh_token)

      },
        error => {
          console.log(error.error);
          if (error.error.error_description == 'Token is not active') {
            this.signOut();
          }
        }
      );
    }

  }

  getrefreshToken() {
    console.log("refreshing token")
    const payload = new HttpParams()
      .append('client_id', environment.oauthClientId)
      .append('grant_type', 'refresh_token')
      .append('refresh_token', this.getRefreshToken())

    console.log(payload)
    return this.http.post(environment.oauthTokenUrl, payload, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    })
  }

  public saveAccessToken(token: any): void {
    this.storage.set('access_token', token);
  }

  public saveRefreshToken(token: any): void {
    this.storage.set('refresh_token', token);
  }

  public signOut(): void {
    const payload = new HttpParams()
      .append('client_id', environment.oauthClientId)
      .append('refresh_token', this.getRefreshToken())

    this.http.post(environment.oauthUrl + "logout", payload, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).subscribe((response: any) => {
      console.log(response);
    });
    this.storage.clearAll();
    console.log("cleared");
    this.router.navigate(['/']);
  }

  // public setUser(employeeId: string, name: string, roles: string, position: string): void {
  //   this.storage.set('id', employeeId);
  //   this.storage.set('name', name);
  //   this.storage.set('roles', roles);
  //   this.storage.set('position', position);
  // }

  // public getUser() {
  //   return {
  //     id: this.storage.get('id'),
  //     name: this.storage.get('name'),
  //     role: this.storage.get('roles'),
  //     position: this.storage.get('position')
  //   }
  // }

  // public getUserId(): number {
  //   return this.storage.get("id")
  // }

}
