package com.sflpro.notifier.services.template.impl;

import com.sflpro.notifier.services.template.TemplatingConfiguration;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/2/19
 * Time: 5:08 PM
 */
public class TemplatingConfigurationImplTest extends AbstractServicesUnitTest {

    @Test
    public void testGetFreemarkerConfiguration() throws IOException {
        File tempFile = null;
        File localizedTempFile = null;
        try {
            final Locale locale = new Locale(uuid(), uuid());
            final String prefix = "dummy";
            final String extension = ".ftl";
            tempFile = File.createTempFile(prefix, extension);
            localizedTempFile = new File(tempFile.getAbsolutePath().replace(extension, "_" + locale.toString() + extension));
            localizedTempFile.createNewFile();
            final TemplatingConfiguration configuration = new TemplatingConfigurationImpl(
                    "file:///" + tempFile.getParent(),
                    extension
            );
            assertThat(configuration.getFreemarkerConfiguration(true).getTemplate(
                    tempFile.getName(),
                    locale,
                    null,
                    StandardCharsets.UTF_8.name(),
                    true,
                    true
                    ).getSourceName()
            )
                    .isEqualTo(localizedTempFile.getName());
            assertThat(configuration.getFreemarkerConfiguration(false).getTemplate(
                    tempFile.getName(),
                    locale,
                    null,
                    StandardCharsets.UTF_8.name(),
                    true,
                    true
                    ).getSourceName()
            )
                    .isEqualTo(tempFile.getName());
            delete(localizedTempFile);
            assertThat(configuration.getFreemarkerConfiguration(true).getTemplate(
                    tempFile.getName(),
                    locale,
                    null,
                    StandardCharsets.UTF_8.name(),
                    true,
                    true
                    ).getSourceName()
            )
                    .isEqualTo(tempFile.getName());

        } finally {
            delete(tempFile);
            delete(localizedTempFile);
        }
    }

    private static void delete(final File file) {
        if (file == null || !file.exists()) {
            return;
        }
        try {
            file.delete();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
}
