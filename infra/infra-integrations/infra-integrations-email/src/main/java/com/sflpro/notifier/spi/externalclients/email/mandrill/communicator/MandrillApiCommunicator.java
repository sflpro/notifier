package com.sflpro.notifier.externalclients.email.mandrill.communicator;

import com.sflpro.notifier.email.TemplatedEmailMessage;

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
     * @param message
     * @return true/false
     */
    void sendEmailTemplate(final TemplatedEmailMessage message);
}
