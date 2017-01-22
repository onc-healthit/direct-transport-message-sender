package org.sitenv.directtransportmessagesender.dto;

/**
 * Created by Brian on 1/19/2017.
 */
public class MessageSenderResult {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
