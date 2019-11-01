package com.sflpro.notifier.externalclients.email.mandrill.communicator;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillEmailClientRuntimeException;
import com.sflpro.notifier.externalclients.email.test.AbstractEmailNotificationIntegrationTest;
import com.sflpro.notifier.spi.email.TemplatedEmailMessage;
import com.sflpro.notifier.spi.email.TemplatedEmailMessageBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
@ContextConfiguration(value = {"classpath:applicationContext-externalclients-email-integrationtest.xml"})
@TestPropertySource(properties = {"mandrill.enabled=true","mandrill.service.token=sx3Ielt1VEGrmi_ANXEFcg"})
public class MandrillApiCommunicatorIntegrationTest extends AbstractEmailNotificationIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MandrillApiCommunicatorIntegrationTest.class);

    /* Dependencies */
    @Autowired
    private MandrillApiCommunicator mandrillApiCommunicator;

    /* Constructors */
    public MandrillApiCommunicatorIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testSendEmailTemplate() {
        // Prepare data
        final String templateName = "integration-test-template";
        final Map<String, Object> templateContent = new HashMap<>();
        templateContent.put("token", "super-secure-token");
        final TemplatedEmailMessage message = new TemplatedEmailMessageBuilder("some_dummy_mail_from@weadapt.digital",
                "some_dummy_mail@weadapt.digital", templateName, templateContent).build();
        // Execute send request
        try {
            mandrillApiCommunicator.sendEmailTemplate(message);
        } catch (final MandrillEmailClientRuntimeException ex) {
            LOGGER.error("Mandrill API call failed.", ex);
            fail(ex.getMessage());
        }
    }

    @Test
    public void testSendEmailTemplateWithNotExistingTemplate() throws MandrillApiError {
        // Prepare data
        final String templateName = RandomStringUtils.randomAlphanumeric(50) + "MEKA-CHEQ-KARA";
        Map<String, Object> templateContent = new HashMap<>();
        templateContent.put("token", "super-secure-token");
        final TemplatedEmailMessage message = new TemplatedEmailMessageBuilder("some_dummy_mail_from@weadapt.digital",
                "some_dummy_mal@weadapt.digital", templateName, templateContent).build();
        // Execute send request
        try {
            mandrillApiCommunicator.sendEmailTemplate(message);
        } catch (final MandrillEmailClientRuntimeException e) {
            // Should throw Unknown template error
            if (e.getCause() instanceof MandrillApiError) {
                assertEquals("Unknown_Template", ((MandrillApiError) e.getCause()).getMandrillErrorName());
            } else {
                throw new MandrillApiError();
            }
        }
    }
}
