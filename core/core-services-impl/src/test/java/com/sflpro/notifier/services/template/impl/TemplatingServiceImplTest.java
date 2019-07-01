package com.sflpro.notifier.services.template.impl;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.template.TemplatingConfiguration;
import com.sflpro.notifier.services.template.TemplatingService;
import com.sflpro.notifier.services.template.TemplatingServiceImpl;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 4/17/19
 * Time: 9:48 PM
 */
public class TemplatingServiceImplTest extends AbstractServicesUnitTest {
    private TemplatingServiceImpl templatingService;

    @Mock
    private TemplatingConfiguration templatingConfiguration;

    @Mock
    private Configuration freemarkerConfiguration;

    @Mock
    private Template template;

    @Before
    public void init() {
        templatingService = new TemplatingServiceImpl(templatingConfiguration);
    }

    @Test
    public void testInitializetion() {
        // Replay
        replayAll();
        // Run test scenario
        TemplatingService testService = new TemplatingServiceImpl(templatingConfiguration);
        // Verify
        verifyAll();
    }

    @Test
    public void testGetContentForTemplateWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            templatingService.getContentForTemplate(null, Collections.emptyMap());
            fail("Exception should be thrown");
        } catch (IllegalArgumentException e) {
            //Ignore
        }
        try {
            templatingService.getContentForTemplate(UUID.randomUUID().toString(), null);
            fail("Exception should be thrown");
        } catch (IllegalArgumentException e) {
            //Ignore
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetContentForTemplateWhenIOException() throws IOException {
        // Test data
        final String templateName = UUID.randomUUID().toString();
        final Throwable cause = new IOException();
        // Reset
        resetAll();
        // Expectations
        expect(templatingConfiguration.getFreemarkerConfiguration(false)).andReturn(freemarkerConfiguration);
        expect(freemarkerConfiguration.getTemplate(templateName, null, StandardCharsets.UTF_8.name(), true, true)).andThrow(cause).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            templatingService.getContentForTemplate(templateName, Collections.emptyMap());
            fail("Exception should be thrown");
        } catch (ServicesRuntimeException e) {
            assertEquals(cause, e.getCause());
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetContentForTemplateWhenTemplateException() throws TemplateException, IOException {
        // Test data
        final String templateName = UUID.randomUUID().toString();
        final Map<String, String> parameters = Collections.emptyMap();
        final Throwable cause = new TemplateException(Environment.getCurrentEnvironment());
        // Reset
        resetAll();
        // Expectations
        expect(templatingConfiguration.getFreemarkerConfiguration(false)).andReturn(freemarkerConfiguration);
        expect(freemarkerConfiguration.getTemplate(templateName, null, StandardCharsets.UTF_8.name(), true, true)).andReturn(template).once();
        template.process(eq(parameters), isA(StringWriter.class));
        expectLastCall().andThrow(cause).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            templatingService.getContentForTemplate(templateName, parameters);
            fail("Exception should be thrown");
        } catch (ServicesRuntimeException e) {
            assertEquals(cause, e.getCause());
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetContentForTemplateIgnoringLocale() throws TemplateException, IOException {
        // Test data
        final String templateName = UUID.randomUUID().toString();
        final Map<String, String> parameters = Collections.emptyMap();
        final String expectedContent = UUID.randomUUID().toString();
        // Reset
        resetAll();
        // Expectations
        expect(templatingConfiguration.getFreemarkerConfiguration(false)).andReturn(freemarkerConfiguration);
        expect(freemarkerConfiguration.getTemplate(templateName, null, StandardCharsets.UTF_8.name(), true, true)).andReturn(template).once();
        template.process(eq(parameters), isA(StringWriter.class));
        expectLastCall().andAnswer(() -> {
            StringWriter writer = (StringWriter) getCurrentArguments()[1];
            writer.write(expectedContent);
            return null;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        final String content = templatingService.getContentForTemplate(templateName, parameters);
        assertEquals(expectedContent, content);
        // Verify
        verifyAll();
    }

    @Test
    public void testGetContentForTemplateProvidingLocale() throws TemplateException, IOException {
        // Test data
        final String templateName = UUID.randomUUID().toString();
        final Map<String, String> parameters = Collections.emptyMap();
        final String expectedContent = UUID.randomUUID().toString();
        // Reset
        resetAll();
        // Expectations
        expect(templatingConfiguration.getFreemarkerConfiguration(true)).andReturn(freemarkerConfiguration);
        expect(freemarkerConfiguration.getTemplate(templateName, Locale.getDefault(), StandardCharsets.UTF_8.name(), true, true)).andReturn(template).once();
        template.process(eq(parameters), isA(StringWriter.class));
        expectLastCall().andAnswer(() -> {
            StringWriter writer = (StringWriter) getCurrentArguments()[1];
            writer.write(expectedContent);
            return null;
        }).once();
        // Replay
        replayAll();
        // Run test scenario
        final String content = templatingService.getContentForTemplate(templateName, parameters,Locale.getDefault());
        assertEquals(expectedContent, content);
        // Verify
        verifyAll();
    }

}
