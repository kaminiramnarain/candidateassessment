import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { AccountTypeEnum } from './shared/model';
import { OauthService } from './shared/service/oauth/oauth.service';
import {LocalStorageService} from "angular-2-local-storage";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  public showNavBar: boolean = true;
  public name: string = '';
  @ViewChild("sidenav") sidenav !: MatSidenav;

  constructor(private router: Router, public auth: OauthService,  private storage: LocalStorageService,) { }

  ngOnInit(): void {
    this.name = this.storage.get('name');
  }

  public navigateToDashboard(): void {
    this.storage.get('isLoggedIn') == "true" ?
      this.router.navigate(['/dashboard']) :
      this.router.navigate(['/']);
  }

  public isHR(): boolean {
    return this.storage.get('role') == AccountTypeEnum.HR;
  }

  public isReviewer(): boolean {
    return this.storage.get('role') == AccountTypeEnum.REVIEWER;
  }

  public isLoggedIn(): boolean {
    return this.storage.get('isLoggedIn')== "true";
  }

  public logUserOut(): void {
    this.auth.signOut();
    this.storage.set("isLoggedIn", "false");
    this.sidenav.close();
  
  }



}