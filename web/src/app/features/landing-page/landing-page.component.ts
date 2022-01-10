import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ValidateTokenDto } from 'src/app/shared/model';
import { PublicService } from 'src/app/shared/service/api/public/public.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit {
  public token!: string;
  public validateTokenDto!: ValidateTokenDto;

  constructor(private publicService: PublicService,
    private router: Router,
    private utilService: UtilsService) { }


  public tokenForm = new FormGroup({
    candidateToken: new FormControl(null, [
      Validators.required,
      Validators.minLength(10) // check if 9 or 10
    ])
  });

  ngOnInit(): void { }


  public onSubmit(): void {
    this.token = this.token.toUpperCase();
    this.publicService
      .validateToken(this.token)
      .subscribe({
        next: (validateToken) => {
          this.validateTokenDto = {
            candidateSelectSkills: validateToken.candidateSelectSkills,
            isAttempted: validateToken.isAttempted,
            userQuestionnaireId: validateToken.userQuestionnaireId
          }
          if (this.validateTokenDto.isAttempted === true) {
            this.utilService.openSnackBar('Token has already been used to attempt questionnaire!', 'Retry');
          } else {
            if (this.validateTokenDto.candidateSelectSkills === true) {
              this.router.navigate([`/select-skills/${this.validateTokenDto.userQuestionnaireId}`]);
            }
            else
              this.router.navigate([`/questionnaire-start/${this.validateTokenDto.userQuestionnaireId}`]);
          }
        },
        error: (error) => {
          this.utilService.openSnackBar('Token is incorrect!', 'Retry');
        },
      });
  }


}


