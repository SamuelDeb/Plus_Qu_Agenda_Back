package fr.sd.reservcreneaux.services;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@QuarkusTest
class MailServiceTest {

    @Mock
    Mailer mailer;

    @InjectMocks
    MailService mailService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Transactional

    void sendEmail() {


        mailService.sendEmail("samuel.debaer@gmail.com", "Test Subject", "Test Body");

        verify(mailer).send(Mail.withHtml("samuel.debaer@gmail.com", "Test Subject", "Test Body"));
    }
}