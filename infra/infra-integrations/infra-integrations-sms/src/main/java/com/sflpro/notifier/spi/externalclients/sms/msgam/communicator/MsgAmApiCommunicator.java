package com.sflpro.notifier.externalclients.sms.msgam.communicator;


import com.sflpro.notifier.externalclients.sms.msgam.model.request.SendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.SendMessagesResponse;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 4:51 PM
 */
public interface MsgAmApiCommunicator {

    SendMessagesResponse sendMessage(final SendMessagesRequest sendMessagesRequest);
}
