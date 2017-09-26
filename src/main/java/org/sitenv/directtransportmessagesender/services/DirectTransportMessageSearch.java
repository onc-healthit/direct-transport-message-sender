package org.sitenv.directtransportmessagesender.services;

import org.sitenv.directtransportmessagesender.dto.ReceivedMessage;

import java.util.List;

public interface DirectTransportMessageSearch {
    List<ReceivedMessage> searchEmailByFromAddress(final String fromAddress);
}
