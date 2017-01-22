package org.sitenv.directtransportmessagesender.controllers;

import org.sitenv.directtransportmessagesender.dto.MessageSenderResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Brian on 1/19/2017.
 */
@RestController
public class MessageSenderController {
    @RequestMapping(value = "/uploadtrustanchor", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public MessageSenderResult sendDirectMessage(){
        MessageSenderResult messageSenderResult = new MessageSenderResult();

        return messageSenderResult;
    }
}
