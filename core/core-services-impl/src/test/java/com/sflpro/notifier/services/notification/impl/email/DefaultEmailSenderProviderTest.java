package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.spi.email.SimpleEmailSenderRegistry;
import com.sflpro.notifier.spi.email.TemplatedEmailSenderRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/2/19
 * Time: 4:18 PM
 */
public class DefaultEmailSenderProviderTest extends AbstractServicesUnitTest {

    private EmailSenderProvider emailSenderProvider;

    private final SimpleEmailSenderRegistry simpleEmailSenderRegistry = SimpleEmailSenderRegistry.of(uuid(), System.out::println);

    private final TemplatedEmailSenderRegistry templatedEmailSenderRegistry = TemplatedEmailSenderRegistry.of(uuid(), System.out::println);

    @Before
    public void prepare() {
        emailSenderProvider = new DefaultEmailSenderProvider(
                Collections.singletonList(simpleEmailSenderRegistry),
                Collections.singletonList(templatedEmailSenderRegistry)
        );
    }

    @Test
    public void testLookupSimpleEmailSenderForWithIllegalArguments() {
        assertThatThrownBy(() -> emailSenderProvider.lookupSimpleEmailSenderFor(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> emailSenderProvider.lookupSimpleEmailSenderFor(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testLookupSimpleEmailSenderMissing() {
        assertThat(emailSenderProvider.lookupSimpleEmailSenderFor(simpleEmailSenderRegistry.name() + "_test"))
                .isEmpty();
    }

    @Test
    public void testLookupSimpleEmailSenderPresent() {
        assertThat(emailSenderProvider.lookupSimpleEmailSenderFor(simpleEmailSenderRegistry.name()))
                .isPresent()
                .contains(simpleEmailSenderRegistry.sender());
    }

    @Test
    public void testLookupTemplatedEmailSenderForWithIllegalArguments() {
        assertThatThrownBy(() -> emailSenderProvider.lookupTemplatedEmailSenderFor(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> emailSenderProvider.lookupTemplatedEmailSenderFor(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testLookupTemplatedEmailSenderForMissing() {
        assertThat(emailSenderProvider.lookupTemplatedEmailSenderFor(templatedEmailSenderRegistry.name() + "_test"))
                .isEmpty();
    }

    @Test
    public void testLookupTemplatedEmailSenderForSender() {
        assertThat(emailSenderProvider.lookupTemplatedEmailSenderFor(templatedEmailSenderRegistry.name()))
                .isPresent()
                .contains(templatedEmailSenderRegistry.sender());
    }
}
