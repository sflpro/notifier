package com.sflpro.notifier.springboot;

import com.sflpro.notifier.db.NotifierPersistenceConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Company: SFL LLC
 * Created on 1/22/18
 *
 * @author Yervand Aghababyan
 */
@ActiveProfiles("test")
@SpringBootApplication
@Import(NotifierPersistenceConfiguration.class)
public class NotifierRepositoryTestApplication {
}
