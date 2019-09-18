package com.sflpro.notifier.api.config;

import com.sflpro.notifier.api.facade.config.SwaggerConfig;
import com.sflpro.notifier.api.internal.facade.test.AbstractFacadeUnitTest;
import io.swagger.jaxrs.config.BeanConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Company: SFL LLC
 * Created on 18/09/2019
 *
 * @author Davit Harutyunyan
 */
public class SwaggerConfigTest extends AbstractFacadeUnitTest {

    private SwaggerConfig swaggerConfig;

    @Before
    public void prepare() {
        swaggerConfig = new SwaggerConfig();
    }

    @Test
    public void testGetConfig() {
        // Run test scenario
        BeanConfig beanConfig = swaggerConfig.getSwaggerConfig();
        // Assertions
        assertEquals("com.sflpro.notifier.api.facade.endpoints", beanConfig.getResourcePackage());
        assertNotNull(beanConfig.getInfo());
    }
}
