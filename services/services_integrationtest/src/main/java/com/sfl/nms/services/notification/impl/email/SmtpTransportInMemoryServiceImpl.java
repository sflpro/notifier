package com.sfl.nms.services.notification.impl.email;

import com.sfl.nms.services.notification.email.SmtpTransportService;
import com.sfl.nms.services.notification.dto.email.MailSendConfiguration;
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
    }

    @Override
    public void sendMessageOverSmtp(@Nonnull final MailSendConfiguration mailSendConfiguration) {
        Assert.notNull(mailSendConfiguration, "Mail send configuration should not be null");
    }
}
