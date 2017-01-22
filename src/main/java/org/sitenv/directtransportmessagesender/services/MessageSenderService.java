package org.sitenv.directtransportmessagesender.services;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.sitenv.directtransportmessagesender.dto.MessageSenderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by Brian on 1/19/2017.
 */
@Service
public class MessageSenderService {
    private JavaMailSender javaMailSender;
    private static final String EMAIL_BODY_TEXT = "Dear User," + "\r\nAttached is the C-CDA document you have selected.";
    private static final String EMAIL_SUBJECT_TEXT = "SITE direct email test";
    @Value("${directFromEndpoint}")
    private String from;

    @Autowired
    public MessageSenderService(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }

    public MessageSenderResult sendMessage(String to, String selectedFile){
        MimeMessage message = javaMailSender.createMimeMessage();
        MessageSenderResult messageSenderResult = new MessageSenderResult();
        messageSenderResult.setSuccess(false);
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(EMAIL_SUBJECT_TEXT);
            helper.setText(EMAIL_BODY_TEXT);

            FileSystemResource file = new FileSystemResource(selectedFile);
            helper.addAttachment(file.getFilename(), file);
            javaMailSender.send(message);
            messageSenderResult.setSuccess(true);
            messageSenderResult.setMessage("Message has been sent to the host successfully - no more information is available. Please check the inbox of " + to + " to ensure delivery.");
        }catch (MessagingException e) {
            messageSenderResult.setSuccess(false);
            messageSenderResult.setMessage("Message not sent with the following error: " + ExceptionUtils.getStackTrace(e));
        }catch (MailSendException e){
            messageSenderResult.setSuccess(false);
            messageSenderResult.setMessage("Could not connect to the mail server with the following error : " + ExceptionUtils.getStackTrace(e));
        }

        return messageSenderResult;
    }
}
