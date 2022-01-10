import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {
  CandidatesWhoAreNotAssignedInterviewDateDto,
  InviteCandidateDto
} from 'src/app/shared/model';
import { UtilsService } from 'src/app/shared/service/utils/utils.service';
import { CandidateService } from 'src/app/shared/service/api/candidate/candidate.service';
import { UserQuestionnaireService } from 'src/app/shared/service/api/user-questionnaire/user-questionnaire.service';
import * as moment from 'moment';

@Component({
  selector: 'app-invite-candidate',
  templateUrl: './invite-candidate.component.html',
  styleUrls: ['./invite-candidate.component.scss']
})
export class InviteCandidateComponent implements OnInit {

  constructor(private candidateService: CandidateService,
    private userQuestionnaireService: UserQuestionnaireService,
    private router: Router,
    private utilService: UtilsService,
  ) {
    this.minDate = new Date();
  }


  public inviteCandidateDto!: InviteCandidateDto;
  public allCandidates: CandidatesWhoAreNotAssignedInterviewDateDto[] = [];
  public searchValueCandidate!: string;
  public candidate!: InviteCandidateDto;
  public minDate!: Date;
  public interviewTime: Date = new Date();
  public interviewDate!: Date;

  public inviteForm = new FormGroup({
    name: new FormControl(null, Validators.required),
    date: new FormControl(new Date(), [
      Validators.required
    ]),
    time: new FormControl(null,
      Validators.required
    )
  });

  ngOnInit(): void {
    this.getCandidates();
  }



  private getCandidates(): void {
    this.candidateService
      .getCandidatesWhoAreNotAssignedInterviewDate()
      .subscribe((candidate) => {
        candidate.forEach((cand) => this.allCandidates.push(cand));
      });
  }

  public candidateOnKey(event: any): void {
    this.searchValueCandidate = event.target.value;
    this.candidateService
      .searchCandidatesWhoAreNotAssignedInterviewDateByName(this.searchValueCandidate)
      .subscribe((candidate) => {
        this.allCandidates.length = 0;
        candidate.forEach((can) => this.allCandidates.push(can));
      });
  }
  public onClose(): void {
    this.searchValueCandidate = "";
    this.getCandidates();
  }

  public onTimeSet(event: any): void {
    var time = event.split(':');
    var hour: number = +time[0];
    var min: number = +time[1];
    this.interviewTime.setHours(hour);
    this.interviewTime.setMinutes(min);
  }

  public onSubmit(): void {

    this.interviewDate.setHours(this.interviewTime.getHours());
    this.interviewDate.setMinutes(this.interviewTime.getMinutes());
    this.interviewDate.setSeconds(0);

    this.inviteCandidateDto = {
      id: this.candidate.id,
      interviewDate: this.interviewDate
    }
    this.userQuestionnaireService.inviteCandidate(this.inviteCandidateDto, this.candidate.id)
      .subscribe({
        next: () => {
          this.router.navigate(['/candidate-dashboard']);
          this.utilService.openSnackBar('Interview invite successfully sent!', 'Dismiss');
        },
        error: (error) => {
          this.utilService.openSnackBar('Failed to send interview invite!', 'Retry');
        }
      });
  }

}
