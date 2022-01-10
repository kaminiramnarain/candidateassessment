package ch.elca.candidateassess.mapper;

import ch.elca.candidateassess.dto.*;
import ch.elca.candidateassess.persistence.entity.UserQuestionnaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = UUIDMapper.class)
public interface UserQuestionnaireMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cheatCount", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "isAttempted", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "questionnaire", ignore = true)
    @Mapping(target = "userArchived", ignore = true)
    UserQuestionnaire mapToUserQuestionnaire(CreateUserQuestionnaireDto createUserQuestionnaireDto);

    @Mapping(target = "positionId", source = "userQuestionnaire.position.id")
    @Mapping(target = "questionnaireId", source = "userQuestionnaire.questionnaire.id")
    UserQuestionnaireDto mapToUserQuestionnaireDto(UserQuestionnaire userQuestionnaire);

    @Mapping(target = "userArchived", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "timeTakenToCompleteQuestionnaire", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "remainingTime", ignore = true)
    @Mapping(target = "questionnaire", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "marks", ignore = true)
    @Mapping(target = "isAttempted", ignore = true)
    @Mapping(target = "interviewDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "cheatCount", ignore = true)
    @Mapping(target = "candidateSelectSkills", ignore = true)
    @Mapping(target = "autoGenerate", ignore = true)
    void mapUpdateUserQuestionnaireDtoToExistingUserQuestionnaire(@MappingTarget UserQuestionnaire userQuestionnaire, UpdateUserQuestionnaireDto updateUserQuestionnaireDto);

    @Mapping(target = "userArchived", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "timeTakenToCompleteQuestionnaire", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "remainingTime", ignore = true)
    @Mapping(target = "questionnaire", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "marks", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "isAttempted", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "emailAddress", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "cheatCount", ignore = true)
    @Mapping(target = "candidateSelectSkills", ignore = true)
    @Mapping(target = "autoGenerate", ignore = true)
    void mapInviteCandidateDtoToExistingUserQuestionnaire(@MappingTarget UserQuestionnaire userQuestionnaire, InviteCandidateDto inviteCandidateDto);

    @Mapping(target = "length", ignore = true)
    @Mapping(target = "marksObtained", source = "userQuestionnaire.marks")
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "timeTaken", source = "userQuestionnaire.timeTakenToCompleteQuestionnaire")
    FilledQuestionnaireDto mapToFilledQuestionnaireDto(@MappingTarget FilledQuestionnaireDto filledQuestionnaireDto, UserQuestionnaire userQuestionnaire);

    @Mapping(target = "userQuestionnaireId", source = "userQuestionnaire.id")
    void mapToValidateToken(@MappingTarget ValidateTokenDto validateTokenDto, UserQuestionnaire userQuestionnaire);

    @Mapping(target = "userArchived", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "timeTakenToCompleteQuestionnaire", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "remainingTime", ignore = true)
    @Mapping(target = "questionnaire", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "isAttempted", ignore = true)
    @Mapping(target = "interviewDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "emailAddress", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "cheatCount", ignore = true)
    @Mapping(target = "candidateSelectSkills", ignore = true)
    @Mapping(target = "autoGenerate", ignore = true)
    void mapSaveReviewedUserQuestionnaireDtoToExistingUserQuestionnaire(@MappingTarget UserQuestionnaire userQuestionnaire, SaveReviewedUserQuestionnaireDto saveReviewedUserQuestionnaireDto);

}