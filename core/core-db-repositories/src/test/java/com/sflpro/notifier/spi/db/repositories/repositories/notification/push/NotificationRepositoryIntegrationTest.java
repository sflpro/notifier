package com.sflpro.notifier.db.repositories.repositories.notification.push;

import com.sflpro.notifier.db.repositories.repositories.notification.NotificationRepository;
import com.sflpro.notifier.test.AbstractRepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Company: SFL LLC
 * Created on 1/22/18
 *
 * @author Yervand Aghababyan
 */
public class NotificationRepositoryIntegrationTest extends AbstractRepositoryTest {
    /* Dependencies */
    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void testRepositoryConstruction() {
        notificationRepository.count();
    }
}
