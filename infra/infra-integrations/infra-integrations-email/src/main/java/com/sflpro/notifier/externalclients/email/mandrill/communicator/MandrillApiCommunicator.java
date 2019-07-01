package com.sflpro.notifier.externalclients.email.mandrill.communicator;


import com.sflpro.notifier.spi.email.SimpleEmailMessage;
import com.sflpro.notifier.spi.email.TemplatedEmailMessage;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
public interface MandrillApiCommunicator {

    /**
     * Send templated email
     *
     * @param message
     */
    void sendEmailTemplate(final TemplatedEmailMessage message);


    /**
     * Send simple email
     *
     * @param message
     */
    void sendEmail(final SimpleEmailMessage message);
}
