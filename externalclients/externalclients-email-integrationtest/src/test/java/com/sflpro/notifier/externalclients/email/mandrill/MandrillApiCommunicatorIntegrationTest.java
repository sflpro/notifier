package com.sflpro.notifier.externalclients.email.mandrill;

import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillEmailClientRuntimeException;
import com.sflpro.notifier.externalclients.email.mandrill.model.request.SendEmailRequest;
import com.sflpro.notifier.externalclients.email.test.AbstractEmailNotificationIntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
@ContextConfiguration(value = {"classpath:applicationContext-externalclients-email-integrationtest.xml"})
public class MandrillApiCommunicatorIntegrationTest extends AbstractEmailNotificationIntegrationTest {

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
        Map<String, String> templateContent = new HashMap<>();
        templateContent.put("token", "super-secure-token");
        final SendEmailRequest sendEmailRequest = new SendEmailRequest("some_dummy_mail@weadapt.digital", templateName, templateContent);
        // Execute send request
        final boolean response = mandrillApiCommunicator.sendEmailTemplate(sendEmailRequest);
        assertTrue(response);
    }

    @Test
    public void testSendEmailTemplateWithNotExistingTemplate() throws MandrillApiError {
        // Prepare data
        final String templateName = RandomStringUtils.randomAlphanumeric(50) + "MEKA-CHEQ-KARA";
        Map<String, String> templateContent = new HashMap<>();
        templateContent.put("token", "super-secure-token");
        final SendEmailRequest sendEmailRequest = new SendEmailRequest("some_dummy_mal@weadapt.digital", templateName, templateContent);
        // Execute send request
        try {
            mandrillApiCommunicator.sendEmailTemplate(sendEmailRequest);
        } catch (final MandrillEmailClientRuntimeException e) {
            // Should throw Unknown template error
            if (e.getCause() instanceof MandrillApiError) {
                assertEquals(((MandrillApiError) e.getCause()).getMandrillErrorName(), "Unknown_Template");
            } else {
                throw new MandrillApiError();
            }
        }
    }
}
