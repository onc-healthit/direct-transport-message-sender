package org.sitenv.directtransportmessagesender.services;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.sitenv.directtransportmessagesender.dto.MessageSenderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

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

    public MessageSenderResult sendMessageWithAttachment(String to, String attachmentFilePath){
        MimeMessage message = javaMailSender.createMimeMessage();
        MessageSenderResult messageSenderResult = new MessageSenderResult();
        messageSenderResult.setSuccess(false);
        try{
            MimeMessageHelper helper = configureMessageHelper(message, to);
            FileSystemResource file = new FileSystemResource(attachmentFilePath);
            helper.addAttachment(file.getFilename(), file);
            javaMailSender.send(message);
            setSuccessfulMessageSenderResult(messageSenderResult);
        }catch (MessagingException | MailSendException e) {
            setUnsuccessfulMessageSenderResult(messageSenderResult, ExceptionUtils.getStackTrace(e));
        }
        return messageSenderResult;
    }

    public MessageSenderResult sendMessageWithAttachment(String to, MultipartFile attachmentFile){
        MimeMessage message = javaMailSender.createMimeMessage();
        MessageSenderResult messageSenderResult = new MessageSenderResult();
        messageSenderResult.setSuccess(false);
        try{
            MimeMessageHelper helper = configureMessageHelper(message, to);
            InputStreamSource inputStreamSource = new ByteArrayResource(attachmentFile.getBytes());
            helper.addAttachment(attachmentFile.getOriginalFilename(), inputStreamSource);
            javaMailSender.send(message);
            setSuccessfulMessageSenderResult(messageSenderResult);
        }catch (MessagingException | MailSendException | IOException e) {
            setUnsuccessfulMessageSenderResult(messageSenderResult, ExceptionUtils.getStackTrace(e));
        }
        return messageSenderResult;
    }

    private MimeMessageHelper configureMessageHelper(MimeMessage message, String to) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(EMAIL_SUBJECT_TEXT);
        helper.setText(EMAIL_BODY_TEXT);
        return helper;
    }

    private void setSuccessfulMessageSenderResult(MessageSenderResult messageSenderResult){
        messageSenderResult.setSuccess(true);
        messageSenderResult.setMessage("Message has been sent to the host successfully - no more information is available. Please check the recipient's inbox to ensure delivery.");
    }

    private void setUnsuccessfulMessageSenderResult(MessageSenderResult messageSenderResult, String error){
        messageSenderResult.setSuccess(false);
        messageSenderResult.setMessage("Message not sent with the following error: " + error);
    }
}
