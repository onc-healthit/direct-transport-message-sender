package org.sitenv.directtransportmessagesender.services;

import org.sitenv.directtransportmessagesender.dto.ReceivedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class DirectSitenvMessageSearcher implements DirectTransportMessageSearch {
    private Properties siteMailProperties;
    private String siteSmtpPassword;
    @Value(("${site.smtpusername}"))
    private String userName;

    @Autowired
    public DirectSitenvMessageSearcher(Properties siteMailProperties, String siteSmtpPassword) {
        this.siteMailProperties = siteMailProperties;
        this.siteSmtpPassword = siteSmtpPassword;
    }

    @Override
    public List<ReceivedMessage> searchEmailByFromAddress(String fromAddress) {
        List<ReceivedMessage> receivedMessages = new ArrayList<>();
        Session session = Session.getInstance(siteMailProperties);
        Store store = null;
        try {
            // connects to the message store
            store = session.getStore("imap");
            session.setDebug(true);
            store.connect(userName, siteSmtpPassword);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            Flags recent = new Flags(Flags.Flag.RECENT);
            FlagTerm recentFlagTerm = new FlagTerm(recent, true);

            SearchTerm sender = new FromTerm(new InternetAddress(fromAddress));
            SearchTerm searchTerm = new AndTerm(sender, recentFlagTerm);


            // performs search through the folder
            Message[] foundMessages = folderInbox.search(searchTerm);

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
        }
        finally {
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
