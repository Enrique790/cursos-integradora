package mx.edu.utez.integradora.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

@Service
public class EmailSender {

    private Resend resend = new Resend("re_68Rb9mUN_MfqPRCt6fcMrfqHbW3pbM8Tm");

    Logger log = LoggerFactory.getLogger(EmailSender.class);

    public void sendEmail(String to, String subject, String html) {

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to(to)
                .subject(subject)
                .html(html)
                .build();
        try {
            CreateEmailResponse data = resend.emails().send(params);

            log.info("Id email: " + data + " Success email send");
        } catch (ResendException e) {
            log.error("Errro to send email", e);
            e.printStackTrace();
        }
    }
}
