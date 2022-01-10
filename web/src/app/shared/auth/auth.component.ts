import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {OauthService} from "../service/oauth/oauth.service";
import {LocalStorageService} from "angular-2-local-storage";

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {

  constructor(    private activatedRoute: ActivatedRoute,
    private auth: OauthService,
    private route: Router,
    private storage: LocalStorageService,) { }

  ngOnInit() {
    if (!this.auth.getAccessToken()) {
      this.auth.reloaded = true;
      this.activatedRoute.queryParams.subscribe(params => {
          if (params.code) {
            console.log(params);
            this.auth.requestAccessToken(params.code, params.session_state);
          }
        },
        error => {
          this.auth.goToLoginPage();
          console.log(error);
          
        },
      );
      if (!this.auth.getRefreshToken()) {
        this.auth.goToLoginPage();
      }

    } else {
      this.route.navigate(['/dashboard']).then(() => {
        window.location.reload();
      });
    }
  }


}
