import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import { OauthService } from '../service/oauth/oauth.service';
import {LocalStorageService} from "angular-2-local-storage";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private auth: OauthService,
    private storage: LocalStorageService,
  ) {
  }
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const currentUser = this.auth.getAccessToken();
    const role=this.storage.get('role');
 
  
    
    if (currentUser) {
      if (this.auth.hasTokenExpired()) {
        console.log(this.auth.hasTokenExpired())
        this.auth.refreshToken();
      }
      if (route.data.roles && route.data.roles.indexOf(role) === -1) {
        this.router.navigate(['**'])
      }
      return !(route.data.roles && route.data.roles.indexOf(role) === -1);
    }

    // not logged in so redirect to login page with the return url
    this.auth.goToLoginPage();
    return false;
  }
}
