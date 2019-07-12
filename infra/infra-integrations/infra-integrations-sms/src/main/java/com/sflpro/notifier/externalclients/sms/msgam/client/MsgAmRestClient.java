package com.sflpro.notifier.externalclients.sms.msgam.client;

import com.sflpro.notifier.externalclients.sms.msgam.model.request.ClientSmsSendMessagesRequest;
import com.sflpro.notifier.externalclients.sms.msgam.model.response.SendMessagesResponse;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 5:02 PM
 */
public interface MsgAmRestClient {

    SendMessagesResponse sendMessage(final ClientSmsSendMessagesRequest sendMessagesRequest);
}
