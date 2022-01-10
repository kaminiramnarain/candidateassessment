package ch.elca.candidateassess.service;

import ch.elca.candidateassess.dto.CredentialsDto;
import ch.elca.candidateassess.dto.LoginDto;
import ch.elca.candidateassess.dto.PersonDto;
import ch.elca.candidateassess.dto.ReviewerDto;

import java.util.List;

public interface PersonService {

    void savePerson(PersonDto personDto);

    List<ReviewerDto> getReviewers();

    List<ReviewerDto> getReviewersByName(String reviewerName);

    LoginDto validateCredentials(CredentialsDto credentialsDto);

}
