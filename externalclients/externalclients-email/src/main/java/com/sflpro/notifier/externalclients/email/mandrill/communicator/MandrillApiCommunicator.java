package com.sflpro.notifier.externalclients.email.mandrill.communicator;

import com.sflpro.notifier.externalclients.email.mandrill.model.request.SendEmailRequest;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
public interface MandrillApiCommunicator {

    /**
     * Send calendar reminder email
     *
     * @param sendEmailRequest
     * @return true/false
     */
    boolean sendEmailTemplate(final SendEmailRequest sendEmailRequest);
}
