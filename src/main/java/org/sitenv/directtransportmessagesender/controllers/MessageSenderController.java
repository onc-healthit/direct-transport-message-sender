package org.sitenv.directtransportmessagesender.controllers;

import org.sitenv.directtransportmessagesender.dto.MessageSenderResult;
import org.sitenv.directtransportmessagesender.services.MessageSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Brian on 1/19/2017.
 */
@RestController
public class MessageSenderController {
    private MessageSenderService messageSenderService;
    @Resource
    private List<String> ccdaFileList;

    @Autowired
    public MessageSenderController(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @RequestMapping(value = "/sendmessagewithattachmentfilepath", method = RequestMethod.POST)
    public MessageSenderResult sendMessageWithAttachment(@RequestParam(value = "toAddress", required = true) String toAddress,
                                                 @RequestParam(value = "attachmentFilePath", required = true) String attachmentFilePath){
        return messageSenderService.sendMessageWithAttachment(toAddress, attachmentFilePath);
    }

    @RequestMapping(value = "/sendmessagewithattachmentfile", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public MessageSenderResult sendMessageWithAttachment(@RequestParam(value = "toAddress", required = true) String toAddress,
                                                 @RequestParam(value = "attachment", required = true) MultipartFile attachment){
        return messageSenderService.sendMessageWithAttachment(toAddress, attachment);
    }

    @RequestMapping(value = "/listsampleccdafiles", method = RequestMethod.GET)
    public List<String> getSampleCcdaList(){
        return ccdaFileList;
    }

}
