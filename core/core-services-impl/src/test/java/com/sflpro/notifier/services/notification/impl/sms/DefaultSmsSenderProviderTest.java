package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.services.notification.impl.DummySimpleSmsSender;
import com.sflpro.notifier.services.notification.impl.DummyTemplatedSmsSender;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.spi.sms.SimpleSmsSenderRegistry;
import com.sflpro.notifier.spi.sms.TemplatedSmsSenderRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/2/19
 * Time: 4:40 PM
 */
public class DefaultSmsSenderProviderTest extends AbstractServicesUnitTest {

    private SmsSenderProvider smsSenderProvider;

    private SimpleSmsSenderRegistry simpleSmsSenderRegistry = SimpleSmsSenderRegistry.of(uuid(), new DummySimpleSmsSender());

    private TemplatedSmsSenderRegistry templatedSmsSenderRegistry = TemplatedSmsSenderRegistry.of(uuid(), new DummyTemplatedSmsSender());

    @Before
    public void prepare() {
        smsSenderProvider = new DefaultSmsSenderProvider(
                Collections.singletonList(simpleSmsSenderRegistry),
                Collections.singletonList(templatedSmsSenderRegistry)
        );
    }

    @Test
    public void testLookupSimpleSmsMessageSenderFor() {
        assertThatThrownBy(() -> smsSenderProvider.lookupSimpleSmsMessageSenderFor(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> smsSenderProvider.lookupSimpleSmsMessageSenderFor(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testLookupSimpleSmsMessageSenderForMissing() {
        assertThat(smsSenderProvider.lookupSimpleSmsMessageSenderFor(simpleSmsSenderRegistry.name() + "_test"))
                .isEmpty();
    }

    @Test
    public void testLookupSimpleSmsMessageSenderForPresent() {
        assertThat(smsSenderProvider.lookupSimpleSmsMessageSenderFor(simpleSmsSenderRegistry.name()))
                .isPresent()
                .contains(simpleSmsSenderRegistry.sender());
    }

    @Test
    public void testLookupTemplatedSmsMessageSenderFor() {
        assertThatThrownBy(() -> smsSenderProvider.lookupTemplatedSmsMessageSenderFor(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> smsSenderProvider.lookupTemplatedSmsMessageSenderFor(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testLookupTemplatedSmsMessageSenderForrMissing() {
        assertThat(smsSenderProvider.lookupTemplatedSmsMessageSenderFor(templatedSmsSenderRegistry.name() + "_test"))
                .isEmpty();
    }

    @Test
    public void testLookupTemplatedSmsMessageSenderForPresent() {
        assertThat(smsSenderProvider.lookupTemplatedSmsMessageSenderFor(templatedSmsSenderRegistry.name()))
                .isPresent()
                .contains(templatedSmsSenderRegistry.sender());
    }

}
