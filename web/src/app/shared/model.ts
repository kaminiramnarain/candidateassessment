/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.22.595 on 2021-11-05 09:30:06.
export class CandidateDto {
  id!: string;
  firstName!: string;
  lastName!: string;
  phoneNumber!: string;
  emailAddress!: string;
  position!: PositionDto;
  userArchived!: boolean;
  remainingTime!: number;
  questionnaireStatus!: QuestionnaireStatusEnum;
  skills!: SelectedSkillDto[];
  reviewers!: ReviewerDto[];
}

export class SelectedSkillDto {
  id!: string;
  name!: string;
  level!: SkillLevelEnum;
}

export class ReviewerDto {
  id!: string;
  firstName!: string;
  lastName!: string;
}

export class SkillDto {
  id!: string;
  name!: string;
  level?: SkillLevelEnum | number;
}

export enum AccountTypeEnum {
  HR = 'HR',
  REVIEWER = 'REVIEWER',
}

export class QuestionDto {
  id!: string;
  questionEnglish!: string;
  questionFrench?: string;
  questionNumber!: number;
  type!: QuestionTypeEnum;
  marks!: number;
  possibleAnswers!: AnswerDto[];
  answerText!: string;
  multipleAnswer!: MultipleAnswerDto[];
}

export class EditQuestionDto {
  id!: string;
  questionEnglish!: string;
  questionType!: QuestionTypeEnum;
  skill!: Skill;
  level!: SkillLevelEnum;
  marks!: number;
  time!: number;
  answers!: AnswerDto[] | EditAnswerDto[];
}

export class EditAnswerDto {
  id!: string;
  answerEnglish!: string;
  answerFrench!: string;
  valid!: boolean;
  constructor(id: string, answerEnglish: string, valid: boolean) {
    this.id = id;
    this.answerEnglish = answerEnglish;
    this.valid = valid;
  }
}

export enum QuestionTypeEnum {
  OPEN_ENDED = 'OPEN_ENDED',
  MULTIPLE_CHOICE = 'MULTIPLE_CHOICE',
  MULTIPLE_ANSWERS = 'MULTIPLE_ANSWERS',
}

export class AnswerDto {
  id!: string;
  answerEnglish!: string;
  answerFrench?: string;
  valid?: boolean;
}

export class MultipleAnswerDto {
  id!: string;
  answerEnglish!: string;
  answerFrench!: string;
  value!: boolean;
  constructor(id: string, value: boolean) {
    this.id = id;
    this.value = value;
  }
}

export class SubmitAnswerDto {
  userQuestionnaireId!: string;
  questionId!: string;
  questionType!: QuestionTypeEnum;
  textAnswer!: string;
  multipleAnswers!: MultipleAnswerDto[];
}

export class InviteCandidateDto {
  id!: string;
  interviewDate!: Date;
}


export class CandidatesWhoAreNotAssignedInterviewDateDto {
  id!: string;
  firstName!: string;
  lastName!: string;
  emailAddress!: string;
}

export class Questionnaire {
  duration!: number;
  remainingTime!: number;
  questions!: IsAnsweredQuestionDto[];
  questionnaireOpen!: boolean;
}

export class IsAnsweredQuestionDto {
  questionNumber!: number;
  isAnswered!: boolean;

  constructor(questionNumber: number, isAnswered: boolean) {
    this.questionNumber = questionNumber;
    this.isAnswered = isAnswered;
  }
}

export class PositionDto {
  id!: string;
  name!: string;
}

export class UserQuestionnaireSkillDto {
  userQuestionnaireId!: string;
  skills!: SkillDto[];
}

export interface DialogData {
  marks: string;
}

export interface CheatDialogData {
  cheat: number;
}

export class CreateReviewDto {
  personIds!: string[];
  status!: ReviewStatusEnum;
  userQuestionnaireId!: string;
}

export enum ReviewStatusEnum {
  NEW = 'NEW',
  IN_PROGRESS = 'IN_PROGRESS',
  REVIEWED = 'REVIEWED',
}

export class Page<T> {
  content!: T;
  totalPages!: number;
  totalElements!: number;
}


export class ValidateTokenDto {
  candidateSelectSkills!: boolean;
  isAttempted!: boolean;
  userQuestionnaireId!: string;
}


export class CreateUserQuestionnaireDto {
  firstName!: string;
  lastName!: string;
  phoneNumber!: string;
  emailAddress!: string;
  marks?: number;
  timeTakenToCompleteQuestionnaire?: number;
  autoGenerate!: boolean;
  remainingTime!: number;
  token?: string;
  interviewDate?: Date;
  candidateSelectSkills!: boolean;
  status!: QuestionnaireStatusEnum;
  questionnaireId?: string;
  positionId!: string;
}

export class UpdateUserQuestionnaireDto {
  firstName!: string;
  lastName!: string;
  phoneNumber!: string;
  emailAddress!: string;
  positionId!: string;
}

export class UserQuestionnaireDto {
  id!: string;
  firstName!: string;
  lastName!: string;
  phoneNumber!: string;
  emailAddress!: string;
  marks?: number;
  timeTakenToCompleteQuestionnaire?: number;
  remainingTime!: number;
  token?: string;
  interviewDate?: Date;
  status!: QuestionnaireStatusEnum;
  questionnaireId?: string;
  positionId!: string;
}

export enum QuestionnaireStatusEnum {
  QUESTIONNAIRE_NOT_GENERATED = 'QUESTIONNAIRE_NOT_GENERATED',
  PENDING = 'PENDING',
  UNDER_REVIEW = 'UNDER_REVIEW',
  COMPLETED = 'COMPLETED',
  DISQUALIFIED = 'DISQUALIFIED'
}

export class CreateQuestionnaireDto {
  marks?: number;
  totalTime!: number;
  templateName!: string;
}

export class QuestionnaireDto {
  id!: string;
  marks?: number;
  totalTime!: number;
  templateName!: string;
}

export class NewQuestion {
  id!: string;
  skillId!: string;
  level!: SkillLevelEnum;
  questionType!: QuestionTypeEnum;
  marks!: number;
  time!: number;
  questionEnglish!: string;
  questionFrench!: string;
  multipleAnswers!: MultipleAnswerDto[];

  constructor(id: string) {
    this.id = id;
  }
}

export class QuestionnaireDataForReviewDto {
  candidateAnswerId!: string;
  questionNumber!: number;
  question!: QuestionForReviewDto;
  textAnswer!: string;
  marksAllocated?: number;
}


export class SaveCandidateMarksDto {
  candidateAnswerId!: string;
  marksAllocated!: number;
}

export class QuestionForReviewDto {
  id!: string;
  questionEnglish!: string;
  questionFrench!: string;
  marks!: number;
}

export class SaveReviewedUserQuestionnaireDto {
  comment!: string;
  marks!: number;
}

export class CandidateReviewDto {
  firstName!: string;
  lastName!: string;
  emailAddress!: string;
  interviewDate!: Date;
  questionnaire!: QuestionnaireDto;
  timeTakenToCompleteQuestionnaire!: number;
  marks!: number;
  remainingTime!: number;
}


export enum SkillLevelEnum {
  NOVICE = "NOVICE",
  BEGINNER = "BEGINNER",
  PROFICIENT = "PROFICIENT",
  ADVANCED = "ADVANCED",
  EXPERT = "EXPERT"
}

export class ViewQuestionDto {
  id!: string;
  questionEnglish!: string;
  questionType!: QuestionTypeEnum;
  skill!: Skill;
  level!: SkillLevelEnum;
  marks!: number;
  time!: number;
  answers!: AnswerDto[];
}

export class Skill {
  id!: string;
  name!: string;
}

export class FilledQuestionnaireDto {
  questions!: FilledQuestionDto[];
  firstName!: string;
  lastName!: string;
  marksObtained!: number;
  marks!: number;
  length!: number;
  timeTaken!: number;
}

export class FilledQuestionDto {
  questionNumber!: number;
  question!: string;
  questionType!: QuestionTypeEnum;
  skillName!: string;
  skillLevel!: SkillLevelEnum;
  answerText!: string;
  answers!: FilledAnswerDto[];
  marksObtained !: number;
  marks!: number;
}

export class FilledAnswerDto {
  answerEnglish!: string;
  selected!: boolean;
  valid!: boolean;
}

export class CredentialsDto {
  emailAddress!: string;
  password!: string;
}

export class LoginDto {
  id!: string;
  firstName!: string;
  lastName!: string;
  emailAddress!: string;
  accountType!: AccountTypeEnum;
}

export class DashboardDto {
  skillName!: string;
  numberOfOpenEndedQuestions!: number;
  numberOfMultipleChoiceQuestions!: number;
  numberOfMultipleAnswersQuestions!: number;
}