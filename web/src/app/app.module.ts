import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { PageNotFoundComponent } from './features/page-not-found/page-not-found.component';
import { LandingPageComponent } from './features/landing-page/landing-page.component';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSliderModule } from '@angular/material/slider';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatChipsModule } from '@angular/material/chips';
import { TestCompletedComponent } from './features/candidate/test-completed/test-completed.component';
import { MatDialogModule } from '@angular/material/dialog';
import { QuillModule } from 'ngx-quill';
import { SelectSkillsComponent } from './features/select-skills/select-skills.component';
import { QuestionNumberComponent } from './features/candidate/questionnaire/question-number/question-number.component';
import { QuestionnaireComponent } from './features/candidate/questionnaire/questionnaire.component';
import { MarksDialogComponent } from './features/candidate/marks-dialog/marks-dialog.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { CandidateProfileComponent } from './features/hr/candidate-profile/candidate-profile.component';
import { CandidateDashboardComponent } from './features/hr/candidate-dashboard/candidate-dashboard.component';
import { MatTableModule } from '@angular/material/table';
import { DeleteDialogComponent } from './features/hr/candidate-dashboard/delete-dialog/delete-dialog.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { RouterModule, Routes } from '@angular/router';
import { UpdateCandidateProfileComponent } from './features/hr/update-candidate-profile/update-candidate-profile.component';
import { OpenTypeQuestionComponent } from './features/candidate/questionnaire/open-type-question/open-type-question.component';
import { MultipleChoiceQuestionComponent } from './features/candidate/questionnaire/multiple-choice-question/multiple-choice-question.component';
import { MultipleAnswersQuestionComponent } from './features/candidate/questionnaire/multiple-answers-question/multiple-answers-question.component';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CreateQuestionComponent } from './features/hr/create-question/create-question.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { QuestionFormComponent } from './features/hr/create-question/question-form/question-form.component';
import { OpenEndedQuestionComponent } from './features/hr/create-question/question-form/open-ended-question/open-ended-question.component';
import { MultipleChoiceQuestionComponent as MultipleChoiceQuestionCreateComponent } from './features/hr/create-question/question-form/multiple-choice-question/multiple-choice-question.component';
import { MultipleAnswersQuestionComponent as MultipleAnswersQuestionCreateComponent } from './features/hr/create-question/question-form/multiple-answers-question/multiple-answers-question.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { QuestionsDashboardComponent } from './features/hr/questions-dashboard/questions-dashboard.component';
import { NoSanitizePipe } from './shared/service/utils/nosanitizerpipe';
import { DeleteQuestionDialogComponent } from './features/hr/questions-dashboard/delete-question-dialog/delete-question-dialog.component';
import { ViewQuestionDialogComponent } from './features/hr/questions-dashboard/view-question-dialog/view-question-dialog.component';
import { EditQuestionComponent } from './features/hr/questions-dashboard/edit-question/edit-question.component';
import { OpenEndedQuestionEditComponent } from './features/hr/questions-dashboard/edit-question/open-ended-question-edit/open-ended-question-edit.component';
import { MultipleChoiceQuestionEditComponent } from './features/hr/questions-dashboard/edit-question/multiple-choice-question-edit/multiple-choice-question-edit.component';
import { MultipleAnswersQuestionEditComponent } from './features/hr/questions-dashboard/edit-question/multiple-answers-question-edit/multiple-answers-question-edit.component';
import { QuestionnaireStartComponent } from './features/candidate/questionnaire-start/questionnaire-start.component';
import { InviteCandidateComponent } from './features/hr/invite-candidate/invite-candidate.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { ReviewAnswersComponent } from './features/reviewer/review-answers/review-answers.component';
import { TextFieldModule } from '@angular/cdk/text-field';
import { ReviewDashboardComponent } from './features/reviewer/review-dashboard/review-dashboard/review-dashboard.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ViewCandidateProfileComponent } from './features/hr/candidate-dashboard/view-candidate-profile/view-candidate-profile.component';
import { NgApexchartsModule } from "ng-apexcharts";
import { ViewCandidateQuestionnaireComponent } from './features/hr/candidate-dashboard/view-candidate-questionnaire/view-candidate-questionnaire.component';
import { LoginComponent } from './features/login/login.component';
import { SelectQuestionsComponent } from './features/reviewer/select-questions/select-questions/select-questions.component';
import { CustomizeQuestionnaireDashboardComponent } from './features/reviewer/customize-questionnaire-dashboard/customize-questionnaire-dashboard/customize-questionnaire-dashboard.component';
import { CheatDialogComponent } from './features/candidate/questionnaire/cheat-dialog/cheat-dialog/cheat-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    PageNotFoundComponent,
    LandingPageComponent,
    SelectSkillsComponent,
    TestCompletedComponent,
    QuestionnaireComponent,
    MarksDialogComponent,
    QuestionNumberComponent,
    CandidateProfileComponent,
    CandidateDashboardComponent,
    DeleteDialogComponent,
    UpdateCandidateProfileComponent,
    OpenTypeQuestionComponent,
    MultipleChoiceQuestionComponent,
    MultipleAnswersQuestionComponent,
    CreateQuestionComponent,
    QuestionFormComponent,
    OpenEndedQuestionComponent,
    MultipleChoiceQuestionCreateComponent,
    MultipleAnswersQuestionCreateComponent,
    QuestionsDashboardComponent,
    NoSanitizePipe,
    DeleteQuestionDialogComponent,
    ViewQuestionDialogComponent,
    EditQuestionComponent,
    OpenEndedQuestionEditComponent,
    MultipleChoiceQuestionEditComponent,
    MultipleAnswersQuestionEditComponent,
    InviteCandidateComponent,
    QuestionnaireStartComponent,
    ReviewAnswersComponent,
    ReviewDashboardComponent,
    ViewCandidateQuestionnaireComponent,
    ViewCandidateProfileComponent,
    LoginComponent,
    SelectQuestionsComponent,
    CustomizeQuestionnaireDashboardComponent,
    CheatDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatToolbarModule,
    MatSidenavModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatCardModule,
    MatListModule,
    MatGridListModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatSliderModule,
    MatAutocompleteModule,
    NgxMatSelectSearchModule,
    DragDropModule,
    MatChipsModule,
    MatDialogModule,
    QuillModule,
    MatIconModule,
    MatSnackBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatCheckboxModule,
    RouterModule,
    MatRadioModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    MatExpansionModule,
    MatSlideToggleModule,
    MatDatepickerModule,
    MatNativeDateModule,
    NgxMaterialTimepickerModule,
    TextFieldModule,
    MatTooltipModule,
    NgApexchartsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
