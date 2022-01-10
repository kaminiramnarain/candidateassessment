import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { QuestionnaireComponent } from './features/candidate/questionnaire/questionnaire.component';
import { TestCompletedComponent } from './features/candidate/test-completed/test-completed.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { CandidateDashboardComponent } from './features/hr/candidate-dashboard/candidate-dashboard.component';
import { CandidateProfileComponent } from './features/hr/candidate-profile/candidate-profile.component';
import { UpdateCandidateProfileComponent } from './features/hr/update-candidate-profile/update-candidate-profile.component';
import { CreateQuestionComponent } from './features/hr/create-question/create-question.component';
import { LandingPageComponent } from './features/landing-page/landing-page.component';
import { PageNotFoundComponent } from './features/page-not-found/page-not-found.component';
import { SelectSkillsComponent } from './features/select-skills/select-skills.component';
import { InviteCandidateComponent } from './features/hr/invite-candidate/invite-candidate.component';
import { ReviewAnswersComponent } from './features/reviewer/review-answers/review-answers.component';
import { QuestionnaireStartComponent } from './features/candidate/questionnaire-start/questionnaire-start.component';
import { QuestionsDashboardComponent } from './features/hr/questions-dashboard/questions-dashboard.component';
import { EditQuestionComponent } from './features/hr/questions-dashboard/edit-question/edit-question.component';
import { ReviewDashboardComponent } from './features/reviewer/review-dashboard/review-dashboard/review-dashboard.component';
import { ViewCandidateQuestionnaireComponent } from './features/hr/candidate-dashboard/view-candidate-questionnaire/view-candidate-questionnaire.component';
import { ViewCandidateProfileComponent } from './features/hr/candidate-dashboard/view-candidate-profile/view-candidate-profile.component';
import { LoginComponent } from './features/login/login.component';
import { SelectQuestionsComponent } from './features/reviewer/select-questions/select-questions/select-questions.component';
import { CustomizeQuestionnaireDashboardComponent } from './features/reviewer/customize-questionnaire-dashboard/customize-questionnaire-dashboard/customize-questionnaire-dashboard.component';
import { AuthComponent } from './shared/auth/auth.component';
import { AuthGuard } from './shared/auth/auth.guard';

const routes: Routes = [
  { path: 'auth', component: AuthComponent },
  { path: '', component: LandingPageComponent },
  { path: 'login', canActivate: [AuthGuard], component: LoginComponent },
  { path: 'dashboard', canActivate: [AuthGuard], component: DashboardComponent },
  { path: 'review-dashboard', canActivate: [AuthGuard], data: { roles: ['REVIEWER'] }, component: ReviewDashboardComponent },
  { path: 'questionnaire-start/:id', component: QuestionnaireStartComponent },
  { path: 'review-answers/:id', canActivate: [AuthGuard], data: { roles: ['REVIEWER'] }, component: ReviewAnswersComponent },
  { path: 'invite-candidate', canActivate: [AuthGuard], data: { roles: ['HR'] }, component: InviteCandidateComponent },
  { path: 'update-candidate-profile/:id', canActivate: [AuthGuard], data: { roles: ['HR'] }, component: UpdateCandidateProfileComponent },
  { path: 'candidate-profile', canActivate: [AuthGuard], data: { roles: ['HR'] }, component: CandidateProfileComponent },
  { path: 'candidate-dashboard', canActivate: [AuthGuard], data: { roles: ['HR'] }, component: CandidateDashboardComponent },
  { path: 'customize-questionnaire-dashboard', canActivate: [AuthGuard], data: { roles: ['REVIEWER'] }, component: CustomizeQuestionnaireDashboardComponent },
  { path: 'select-questions/:id', canActivate: [AuthGuard], data: { roles: ['REVIEWER'] }, component: SelectQuestionsComponent },
  { path: 'test-completed/:id', component: TestCompletedComponent },
  { path: 'page-not-found', component: PageNotFoundComponent },
  { path: 'select-skills/:id', component: SelectSkillsComponent }, //both HR and candidate can access
  { path: 'questionnaire/:id', component: QuestionnaireComponent },
  { path: 'create-question', canActivate: [AuthGuard], data: { roles: ['REVIEWER', 'HR'] }, component: CreateQuestionComponent },
  { path: 'questions-dashboard', canActivate: [AuthGuard], data: { roles: ['REVIEWER', 'HR'] }, component: QuestionsDashboardComponent },
  { path: 'questions/:id', canActivate: [AuthGuard], data: { roles: ['REVIEWER', 'HR'] }, component: EditQuestionComponent },
  { path: 'view-candidate-questionnaire/:id', data: { roles: ['HR'] }, canActivate: [AuthGuard], component: ViewCandidateQuestionnaireComponent },
  { path: 'view-candidate-profile/:id', data: { roles: ['HR'] }, canActivate: [AuthGuard], component: ViewCandidateProfileComponent },
  { path: '**', redirectTo: 'page-not-found', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
