package com.sfl.nms.externalclients.sms.twillio.communicator;

import com.sfl.nms.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sfl.nms.externalclients.sms.twillio.model.response.SendMessageResponse;

import javax.annotation.Nonnull;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 12/25/14
 * Time: 6:50 PM
 */
public interface TwillioApiCommunicator {

    /**
     * Send sms message
     *
     * @param request
     * @return sendMessageResponse
     */
    @Nonnull
    SendMessageResponse sendMessage(@Nonnull final SendMessageRequest request);
}
