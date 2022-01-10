import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { PublicService } from 'src/app/shared/service/api/public/public.service';


@Component({
  selector: 'app-questionnaire-start',
  templateUrl: './questionnaire-start.component.html',
  styleUrls: ['./questionnaire-start.component.scss']
})
export class QuestionnaireStartComponent implements OnInit {

  public userQuestionnaireId!: string;

  constructor(private router: Router, private route: ActivatedRoute, private publicService: PublicService) { 
    if (!(this.route.snapshot.params['id'] === undefined))
    this.userQuestionnaireId = this.route.snapshot.params['id'];
  }

  ngOnInit(): void {
    this.checkId;
  }

  
  public checkId(): void {
    this.publicService.validateId(this.userQuestionnaireId).subscribe({
      next: () => {
      },
      error: (error) => {
        this.router.navigate(['/page-not-found']);
      },
    });
  }
}
