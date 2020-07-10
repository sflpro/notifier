package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.services.notification.impl.template.LocalTemplateContentResolver;
import com.sflpro.notifier.services.template.TemplatingService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.spi.template.TemplateContent;
import com.sflpro.notifier.spi.template.TemplateContentResolver;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.easymock.EasyMock.expect;

public class LocalTemplateContentResolverTest extends AbstractServicesUnitTest {

    private TemplateContentResolver resolver;

    @Mock
    private TemplatingService templatingService;


    @Before
    public void prepare() {
        resolver = new LocalTemplateContentResolver(templatingService);
    }

    @Test
    public void resolveWithIllegalArguments() {
        replayAll();
        assertThatThrownBy(() -> resolver.resolve(null, Collections.emptyMap())).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> resolver.resolve("test", null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> resolver.resolve("test", Collections.emptyMap(), null)).isInstanceOf(IllegalArgumentException.class);
        verifyAll();
    }

    @Test
    public void resolveWithLocaleProvided() {
        final String templateId = uuid();
        final Map<String, ?> variables = Collections.singletonMap(uuid(), uuid());
        final String subject = uuid();
        final String body = uuid();
        final Locale locale = Locale.forLanguageTag("hy");
        expect(templatingService.getContentForTemplate(templateId + "_subject", variables, locale)).andReturn(subject);
        expect(templatingService.getContentForTemplate(templateId + "_content", variables, locale)).andReturn(body);
        replayAll();
        final TemplateContent content = resolver.resolve(templateId, variables, locale);
        assertThat(content).hasFieldOrPropertyWithValue("subject", subject).hasFieldOrPropertyWithValue("body", body);
        verifyAll();
    }

    @Test
    public void resolveWithoutLocaleProvided() {
        final String templateId = uuid();
        final Map<String, ?> variables = Collections.singletonMap(uuid(), uuid());
        final String subject = uuid();
        final String body = uuid();
        expect(templatingService.getContentForTemplate(templateId + "_subject", variables)).andReturn(subject);
        expect(templatingService.getContentForTemplate(templateId + "_content", variables)).andReturn(body);
        replayAll();
        final TemplateContent content = resolver.resolve(templateId, variables);
        assertThat(content).hasFieldOrPropertyWithValue("subject", subject).hasFieldOrPropertyWithValue("body", body);
        verifyAll();
    }
}
