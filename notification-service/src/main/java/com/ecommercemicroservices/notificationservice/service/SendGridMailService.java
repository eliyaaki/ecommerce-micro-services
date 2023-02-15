package com.ecommercemicroservices.notificationservice.service;

import com.sendgrid.helpers.mail.*;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class SendGridMailService implements EmailService{

    @Value("${sendgrid.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.sender}")
    private String sendGridSender;

    @Override
    /*
     * @param title
     * @param body
     * @param to a list of recipients
     */
    public void sendSimpleEmail(String title, String body, List<String> recipients) {
        if (recipients.size() == 0) {
            log.debug("Cannot send email: empty 'recipients' list");
            return;
        }

        Email fromMail = new Email(sendGridSender);
        Content content = new Content("text/html", body);

        var mail = new Mail();
        mail.setFrom(fromMail);
        mail.setSubject(title);
        mail.addContent(content);

        var personalization = new Personalization();

        /* PREPARE SEND LIST ****/
        for (var t: recipients) {
            personalization.addTo(new Email(t));
        }
        mail.addPersonalization(personalization);

        /* SENDING EMAIL ****/
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            /* SEND POST REQUEST TO SENDGRID ****/
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                log.error("Email sending error: {}, {}", response.getStatusCode(), response.getBody());
            }
            log.debug("Email sent to " + String.join(", ", recipients));
        } catch (IOException ex) {
            log.error("Failed to send email: " + ex.getMessage());
        }
    }
}
