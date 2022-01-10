import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountTypeEnum, LoginDto } from 'src/app/shared/model';
import { AuthenticationService } from 'src/app/shared/service/api/authentication/authentication.service';
import { UserQuestionnaireService } from 'src/app/shared/service/api/user-questionnaire/user-questionnaire.service';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public emailAddress!: string;
  public password!: string;
  public data!: LoginDto;
  public showPassword: boolean = false;

  constructor(private userQuestionnaireService: UserQuestionnaireService,
    private router: Router,
    private utilService: UtilsService, private authenticationService: AuthenticationService) { }


  public tokenForm = new FormGroup({
    emailAddress: new FormControl(null, [
      Validators.required,
      Validators.email
    ]),
    password: new FormControl(null, [
      Validators.required,
      Validators.minLength(8)
    ])
  });

  ngOnInit(): void {
    this.initialiseData();
  }

  private initialiseData(): void {
    this.data = {
      id: '',
      firstName: '',
      lastName: '',
      accountType: AccountTypeEnum.HR,
      emailAddress: ''
    };
  }

  public onSubmit(): void {
    this.authenticationService.login(this.emailAddress, this.password).subscribe({
      next: (data) => {
        this.data = data;
        localStorage.setItem("personId", data.id);
        localStorage.setItem("firstName", data.firstName);
        localStorage.setItem("lastName", data.lastName);
        localStorage.setItem("accountType", data.accountType);
        localStorage.setItem("isLoggedIn", "true");
        this.router.navigate(['/dashboard']).then(() => window.location.reload());
      },
      error: (data) => {
        this.utilService.openSnackBar(data.error.message, "Retry");
      }
    });
  }


}
