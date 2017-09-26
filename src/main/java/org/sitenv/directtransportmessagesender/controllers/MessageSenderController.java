package org.sitenv.directtransportmessagesender.controllers;

import org.sitenv.directtransportmessagesender.dto.MessageSenderResult;
import org.sitenv.directtransportmessagesender.dto.ReceivedMessage;
import org.sitenv.directtransportmessagesender.services.DirectHhsMessageSearcher;
import org.sitenv.directtransportmessagesender.services.DirectSitenvMessageSearcher;
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
    private DirectSitenvMessageSearcher directSitenvMessageSearcher;
    private DirectHhsMessageSearcher directHhsMessageSearcher;
    @Resource
    private List<String> ccdaFileList;

    @Autowired
    public MessageSenderController(MessageSenderService messageSenderService, DirectSitenvMessageSearcher directSitenvMessageSearcher, DirectHhsMessageSearcher directHhsMessageSearcher) {
        this.messageSenderService = messageSenderService;
        this.directSitenvMessageSearcher = directSitenvMessageSearcher;
        this.directHhsMessageSearcher = directHhsMessageSearcher;
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

    @RequestMapping(value = "/searchsiteinbox", method = RequestMethod.GET)
    public List<ReceivedMessage> searchSiteInbox(@RequestParam(value = "fromAddress") String fromAddress){
        return directSitenvMessageSearcher.searchEmailByFromAddress(fromAddress);
    }

    @RequestMapping(value = "/searchhhsinbox", method = RequestMethod.GET)
    public List<ReceivedMessage> searchHhsInbox(@RequestParam(value = "fromAddress") String fromAddress){
        return directHhsMessageSearcher.searchEmailByFromAddress(fromAddress);
    }
}
