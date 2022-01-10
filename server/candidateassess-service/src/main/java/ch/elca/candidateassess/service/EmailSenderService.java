package ch.elca.candidateassess.service;

public interface EmailSenderService {

    public void sendEmailToCandidate(String toEmail, String subject, String body);
}
