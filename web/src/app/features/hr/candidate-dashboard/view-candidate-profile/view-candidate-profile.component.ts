import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CandidateDto, QuestionnaireStatusEnum, SelectedSkillDto, SkillLevelEnum } from 'src/app/shared/model';
import { CandidateService } from 'src/app/shared/service/api/candidate/candidate.service';
import {
  ApexAxisChartSeries, ApexTitleSubtitle, ApexChart, ApexXAxis, ChartComponent, ApexOptions,
} from "ng-apexcharts";
import { STICKY_POSITIONING_LISTENER } from '@angular/cdk/table';
import { UserQuestionnaireService } from 'src/app/shared/service/api/user-questionnaire/user-questionnaire.service';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  title: ApexTitleSubtitle;
  xaxis: ApexXAxis;
  options: ApexOptions;
};

@Component({
  selector: 'app-view-candidate-profile',
  templateUrl: './view-candidate-profile.component.html',
  styleUrls: ['./view-candidate-profile.component.scss']
})
export class ViewCandidateProfileComponent implements OnInit {

  @ViewChild("chart") chart!: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  public skillLevels!: number[];
  public skillNames!: string[];
  public candidate!: CandidateDto;

  constructor(private router: Router, private candidateService: CandidateService, private route: ActivatedRoute, private userQuestionnaireService: UserQuestionnaireService) {
    this.chartOptions = {
      series: [
        {
          data: [],
          name: "Level"
        }
      ],
      chart: {
        toolbar: {
          show: false
        },
        height: 450,
        type: "radar"
      },
      xaxis: {
        categories: this.skillNames,
        labels: {
          style: {
            fontSize: '16px',
            colors: ['black', 'black', 'black', 'black', 'black']
          }
        }
      }
    };
  }

  ngOnInit(): void {
    this.checkId();
    this.initialiseCandidate();
    this.candidateService.getCandidateById(this.route.snapshot.params['id']).subscribe((candidate) => {
      this.candidate = candidate;
      this.skillLevels = [];
      this.skillNames = [];
      candidate.skills.forEach(skill => {
        this.skillNames.push(skill.name);
        var level = 1;
        if (skill.level == SkillLevelEnum.BEGINNER) level = 2;
        else if (skill.level == SkillLevelEnum.PROFICIENT) level = 3;
        else if (skill.level == SkillLevelEnum.ADVANCED) level = 4;
        else if (skill.level == SkillLevelEnum.EXPERT) level = 5;
        this.skillLevels.push(level);
      });
      this.chartOptions = {
        series: [
          {
            data: this.skillLevels,
            name: "Level"
          }
        ],
        chart: {
          toolbar: {
            show: false
          },
          height: 450,
          type: "radar"
        },
        xaxis: {
          categories: this.skillNames,
          labels: {
            style: {
              fontSize: '16px',
              colors: ['black', 'black', 'black', 'black', 'black'],
              fontFamily: 'proxima-nova',
            },
          },
        }
      };
    });
  }

  public checkId(): void {
    this.userQuestionnaireService.validateId(this.route.snapshot.params['id']).subscribe({
      next: () => {
      },
      error: (error) => {
        this.router.navigate(['/page-not-found']);
      },
    });
  }

  private initialiseCandidate(): void {
    this.candidate = {
      id: '',
      remainingTime:0,
      firstName: '',
      lastName: '',
      emailAddress: '',
      phoneNumber: '',
      position: {
        id: '',
        name: ''
      },
      userArchived: false,
      questionnaireStatus: QuestionnaireStatusEnum.QUESTIONNAIRE_NOT_GENERATED,
      skills: [],
      reviewers: [{ id: '', firstName: '', lastName: '' }]
    }
  }

  public showGraph(skills: SelectedSkillDto[]): boolean {
    return skills.length > 0;
  }

}
