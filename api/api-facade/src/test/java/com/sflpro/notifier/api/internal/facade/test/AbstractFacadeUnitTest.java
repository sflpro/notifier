package com.sflpro.notifier.api.internal.facade.test;

import com.sflpro.notifier.api.internal.facade.helper.ServiceFacadeImplTestHelper;
import com.sflpro.notifier.core.api.internal.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.core.api.internal.model.common.result.ErrorType;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: Alfred Kaghyan
 * Company: SFL LLC
 * Date: 5/13/2015
 * Time: 3:52 PM
 */
@RunWith(EasyMockRunner.class)
@Ignore
public abstract class AbstractFacadeUnitTest extends EasyMockSupport {

    /* Properties */
    private final ServiceFacadeImplTestHelper serviceFacadeImplTestHelper;


    public AbstractFacadeUnitTest() {
        serviceFacadeImplTestHelper = new ServiceFacadeImplTestHelper();
    }

    /* Utility methods */
    protected void assertErrorExists(final List<ErrorResponseModel> errors, final ErrorType errorType) {
        final MutableBoolean mutableBoolean = new MutableBoolean(Boolean.FALSE);
        errors.forEach(error -> {
            if (errorType.equals(error.getErrorType())) {
                mutableBoolean.setTrue();
            }
        });
        assertTrue(mutableBoolean.booleanValue());
    }

    /* Getters and setters */
    public ServiceFacadeImplTestHelper getServiceFacadeImplTestHelper() {
        return serviceFacadeImplTestHelper;
    }
}
