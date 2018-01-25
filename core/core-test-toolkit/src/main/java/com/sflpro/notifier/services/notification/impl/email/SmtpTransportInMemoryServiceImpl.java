package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.services.notification.dto.email.MailSendConfiguration;
import com.sflpro.notifier.services.notification.email.SmtpTransportService;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 6:36 PM
 */
public class SmtpTransportInMemoryServiceImpl implements SmtpTransportService {

    /* Constructors */
    public SmtpTransportInMemoryServiceImpl() {
        super();
    }

    @Override
    public void sendMessageOverSmtp(@Nonnull final MailSendConfiguration mailSendConfiguration) {
        Assert.notNull(mailSendConfiguration, "Mail send configuration should not be null");
    }
}
