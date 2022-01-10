package ch.elca.candidateassess.service.impl;

import ch.elca.candidateassess.dto.CredentialsDto;
import ch.elca.candidateassess.dto.LoginDto;
import ch.elca.candidateassess.dto.PersonDto;
import ch.elca.candidateassess.dto.ReviewerDto;
import ch.elca.candidateassess.exception.ResourceNotFoundException;
import ch.elca.candidateassess.mapper.PersonMapper;
import ch.elca.candidateassess.persistence.entity.Person;
import ch.elca.candidateassess.persistence.entity.QPerson;
import ch.elca.candidateassess.persistence.enumeration.AccountTypeEnum;
import ch.elca.candidateassess.persistence.repository.PersonRepository;
import ch.elca.candidateassess.service.PersonService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public void savePerson(PersonDto personDto) {
        Person person = personMapper.mapToPerson(personDto);
        personRepository.save(person);
    }

    @Override
    public List<ReviewerDto> getReviewers() {
        BooleanBuilder reviewerPredicate = buildReviewPredicate();
        List<Person> personList = new ArrayList<Person>();
        personRepository.findAll(buildReviewPredicate()).forEach(personList::add);
        return personList.stream().map(reviewer -> personMapper.mapToReviewerDto(reviewer)).collect(Collectors.toList());
    }

    private BooleanBuilder buildReviewPredicate() {
        var qPerson = QPerson.person;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qPerson.accountType.eq(AccountTypeEnum.REVIEWER));
    }

    @Override
    public List<ReviewerDto> getReviewersByName(String reviewerName) {
        BooleanBuilder searchPredicate = buildSearchPredicate(reviewerName);
        List<Person> persons = new ArrayList<Person>();
        personRepository.findAll(searchPredicate).forEach(persons::add);
        return persons.stream().map(reviewer -> personMapper.mapToReviewerDto(reviewer)).collect(Collectors.toList());
    }


    private BooleanBuilder buildSearchPredicate(String reviewerName) {
        var qPerson = QPerson.person;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qPerson.accountType.eq(AccountTypeEnum.REVIEWER)).and((qPerson.firstName.concat(" ").concat(qPerson.lastName).toLowerCase().contains(reviewerName.toLowerCase(Locale.ROOT)))
                .or(qPerson.lastName.concat(" ").concat(qPerson.firstName).toLowerCase().contains(reviewerName.toLowerCase(Locale.ROOT))));
    }

    private BooleanBuilder buildCredentialsPredicate(CredentialsDto credentialsDto) {
        var qPerson = QPerson.person;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        return booleanBuilder.and(qPerson.emailAddress.eq(credentialsDto.getEmailAddress())).and(qPerson.password.eq(credentialsDto.getPassword()));
    }

    @Override
    public LoginDto validateCredentials(CredentialsDto credentialsDto) {
        LoginDto loginDto = new LoginDto();
        BooleanBuilder credentialsPredicate = buildCredentialsPredicate(credentialsDto);
        List<Person> persons = new ArrayList<Person>();
        personRepository.findAll(credentialsPredicate).forEach(persons::add);
        persons.stream().findFirst()
                .ifPresentOrElse(person -> {
                    personMapper.mapToLoginDto(loginDto, person);
                }, () -> {
                    throw new ResourceNotFoundException("Credentials are incorrect!");
                });
        return loginDto;
    }

}
