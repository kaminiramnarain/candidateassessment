package ch.elca.candidateassess.dto;

import ch.elca.candidateassess.persistence.enumeration.AccountTypeEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class PersonDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private AccountTypeEnum accountType;
    private String password;

}