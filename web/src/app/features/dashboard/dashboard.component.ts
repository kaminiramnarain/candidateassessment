import { Component, OnInit, ViewChild } from '@angular/core';
import { QuestionService } from 'src/app/shared/service/api/question/question.service';
import {
  ApexAxisChartSeries, ApexChart, ChartComponent, ApexDataLabels, ApexPlotOptions, ApexYAxis, ApexLegend, ApexStroke, ApexXAxis, ApexFill, ApexTooltip
} from "ng-apexcharts";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  fill: ApexFill;
  tooltip: ApexTooltip;
  stroke: ApexStroke;
  legend: ApexLegend;
};

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  @ViewChild("chart") chart!: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  public skillNames: string[] = [];
  public numberOfOpenEndedQuestions: number[] = [];
  public numberOfMultipleChoiceQuestions: number[] = [];
  public numberOfMultipleAnswerQuestions: number[] = [];

  constructor(private questionService: QuestionService) {

    this.chartOptions = {
      series: [
        {
          name: "Open Ended Questions",
          data: []
        },
        {
          name: "Multiple Choice Questions",
          data: []
        },
        {
          name: "Multiple Answers Questions",
          data: []
        }
      ],
      chart: {
        type: "bar",
        height: 500,
        toolbar: {
          show: false
        },
      },
      plotOptions: {
        bar: {
          horizontal: false,
          columnWidth: "55%"
        }
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        show: true,
        width: 2,
        colors: ["transparent"]
      },
      xaxis: {
        categories: []
      },
      yaxis: {
        title: {
          text: " questions"
        }
      },
      fill: {
        opacity: 1
      },
      tooltip: {
        y: {
          formatter: function (val) {
            return val + " questions";
          }
        }
      }
    };
  }

  ngOnInit(): void {
    this.questionService.getDashboardData().subscribe((data) => {
      data.forEach((skill) => {
        this.skillNames.push(skill.skillName);
        this.numberOfOpenEndedQuestions.push(skill.numberOfOpenEndedQuestions);
        this.numberOfMultipleChoiceQuestions.push(skill.numberOfMultipleChoiceQuestions);
        this.numberOfMultipleAnswerQuestions.push(skill.numberOfMultipleAnswersQuestions);
      });

      this.chartOptions = {
        series: [
          {
            name: "Open Ended Questions",
            data: this.numberOfOpenEndedQuestions
          },
          {
            name: "Multiple Choice Questions",
            data: this.numberOfMultipleChoiceQuestions
          },
          {
            name: "Multiple Answers Questions",
            data: this.numberOfMultipleAnswerQuestions
          }
        ],
        chart: {
          type: "bar",
          height: 500,
          toolbar: {
            show: false
          },
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: "55%"
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ["transparent"]
        },
        xaxis: {
          categories: this.skillNames
        },
        yaxis: {
          title: {
            text: " questions"
          }
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val + " questions";
            }
          }
        }
      };

    });
  }

}
