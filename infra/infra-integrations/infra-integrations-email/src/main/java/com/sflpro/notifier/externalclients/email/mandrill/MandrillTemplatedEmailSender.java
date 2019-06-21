package com.sflpro.notifier.externalclients.email.mandrill;


import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.spi.email.TemplatedEmailMessage;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:27 AM
 */
class MandrillTemplatedEmailSender implements TemplatedEmailSender {

    private final MandrillApiCommunicator mandrillApiCommunicator;

    MandrillTemplatedEmailSender(final MandrillApiCommunicator mandrillApiCommunicator) {
        this.mandrillApiCommunicator = mandrillApiCommunicator;
    }

    @Override
    public void send(final TemplatedEmailMessage message) {
        mandrillApiCommunicator.sendEmailTemplate(message);
    }
}
