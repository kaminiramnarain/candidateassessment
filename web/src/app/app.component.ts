import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { AccountTypeEnum } from './shared/model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  public showNavBar: boolean = true;
  public name: string = '';
  @ViewChild("sidenav") sidenav !: MatSidenav;

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.name = localStorage.getItem("firstName") + " " + localStorage.getItem("lastName");
  }

  public navigateToDashboard(): void {
    localStorage.getItem("isLoggedIn") == "true" ?
      this.router.navigate(['/dashboard']) :
      this.router.navigate(['/']);
  }

  public isHR(): boolean {
    return localStorage.getItem("accountType") == AccountTypeEnum.HR;
  }

  public isReviewer(): boolean {
    return localStorage.getItem("accountType") == AccountTypeEnum.REVIEWER;
  }

  public isLoggedIn(): boolean {
    return localStorage.getItem("isLoggedIn") == "true";
  }

  public logUserOut(): void {
    localStorage.clear();
    localStorage.setItem("isLoggedIn", "false");
    this.sidenav.close();
    this.router.navigate(['/login']);
  }

  public onLoginPage(): boolean {
    return this.router.url == "/login";
  }


}