package com.sflpro.notifier.db.repositories.utility;

import com.sflpro.notifier.db.repositories.utility.impl.PersistenceUtilityServiceImpl;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 2:34 PM
 */
final class EntityInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceUtilityServiceImpl.class);

    private EntityInitializer() {
        super();
    }

    static  <T> T initializeAndUnProxy(@Nonnull final T entity) {
        Assert.notNull(entity, "Entity should not be null");
        LOGGER.debug("UnProxying entity - {}", new Object[]{entity});
        // UnProxied entity
        T unProxiedEntity = entity;
        // Initialize
        Hibernate.initialize(unProxiedEntity);
        if (unProxiedEntity instanceof HibernateProxy) {
            unProxiedEntity = (T) ((HibernateProxy) unProxiedEntity).getHibernateLazyInitializer().getImplementation();
        }
        return unProxiedEntity;
    }
}
