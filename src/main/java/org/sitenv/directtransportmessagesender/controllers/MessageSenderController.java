package org.sitenv.directtransportmessagesender.controllers;

import org.sitenv.directtransportmessagesender.dto.MessageSenderResult;
import org.sitenv.directtransportmessagesender.services.MessageSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Brian on 1/19/2017.
 */
@RestController
public class MessageSenderController {
    private MessageSenderService messageSenderService;

    @Autowired
    public MessageSenderController(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @RequestMapping(value = "/sendprecannedccda", method = RequestMethod.POST)
    public MessageSenderResult sendDirectMessage(@RequestParam(value = "toAddress", required = true) String toAddress,
                                                 @RequestParam(value = "ccdaFilePath", required = true) String ccdaFilePath){
        return messageSenderService.sendMessage(toAddress, ccdaFilePath);
    }
}
