package com.sflpro.notifier.externalclients.email.mandrill;

import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.spi.email.SimpleEmailMessage;
import com.sflpro.notifier.spi.email.SimpleEmailSender;
import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/1/19
 * Time: 11:54 AM
 */
class MandrillSimpleEmailSender implements SimpleEmailSender {

    private final MandrillApiCommunicator mandrillApiCommunicator;

    MandrillSimpleEmailSender(final MandrillApiCommunicator mandrillApiCommunicator) {
        this.mandrillApiCommunicator = mandrillApiCommunicator;
    }

    @Override
    public void send(final SimpleEmailMessage message) {
        Assert.notNull(message,"Null was passed as an argument for parameter 'message'.");
        mandrillApiCommunicator.sendEmail(message);
    }
}
