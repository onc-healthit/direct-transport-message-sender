package org.sitenv.directtransportmessagesender.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ReceivedMessage {
    private Date dateSent;
    private Date dateReceived;
    private String subject;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("MM/dd/yyyy hh:mm aa");

    public ReceivedMessage() {
        
    }

    public String getDateSent() {
        if (dateSent == null) {
            return "";
        } else {
            return simpleDateFormat.format(dateSent);
        }
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getDateReceived() {
        if (dateReceived == null) {
            return "";
        } else {
            return simpleDateFormat.format(dateReceived);
        }
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
