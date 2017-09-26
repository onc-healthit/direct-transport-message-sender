package org.sitenv.directtransportmessagesender.services;

import org.sitenv.directtransportmessagesender.dto.ReceivedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class DirectHhsMessageSearcher implements DirectTransportMessageSearch {
    private Properties hhsMailProperties;
    private String hhsSmtpPassword;
    @Value(("${hhs.smtpusername}"))
    private String userName;

    @Autowired
    public DirectHhsMessageSearcher(Properties hhsMailProperties, String hhsSmtpPassword) {
        this.hhsMailProperties = hhsMailProperties;
        this.hhsSmtpPassword = hhsSmtpPassword;
    }

    @Override
    public List<ReceivedMessage> searchEmailByFromAddress(String fromAddress) {
        List<ReceivedMessage> receivedMessages = new ArrayList<>();
        Session session = Session.getInstance(hhsMailProperties);
        Store store = null;
        try {
            // connects to the message store
            store = session.getStore("imap");
            store.connect(userName, hhsSmtpPassword);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            SearchTerm sender = new FromTerm(new InternetAddress(fromAddress));

            // performs search through the folder
            Message[] foundMessages = folderInbox.search(sender);

            if(foundMessages.length == 0){
                System.out.println("No messages found for " + fromAddress);
            }else{
                for (int i = 0; i < foundMessages.length; i++) {
                    Message message = foundMessages[i];
                    ReceivedMessage receivedMessage = new ReceivedMessage();
                    receivedMessage.setDateReceived(message.getReceivedDate());
                    receivedMessage.setDateSent(message.getSentDate());
                    receivedMessage.setSubject(message.getSubject());
                    receivedMessages.add(receivedMessage);
                }
            }
            // disconnect
            folderInbox.close(false);
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store.");
            ex.printStackTrace();
        }finally {
            if(store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return receivedMessages;
    }
}
