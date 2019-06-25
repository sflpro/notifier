package com.sflpro.notifier.externalclients.sms.nikitamobile.communicator;

import com.sflpro.notifier.externalclients.sms.nikitamobile.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.response.SendMessageResponse;

import javax.annotation.Nonnull;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 12/25/14
 * Time: 6:50 PM
 */
public interface NikitamobileApiCommunicator {

    /**
     * Send sms message
     *
     * @param request
     * @return sendMessageResponse
     */
    @Nonnull
    SendMessageResponse sendMessage(@Nonnull final SendMessageRequest request);
}
