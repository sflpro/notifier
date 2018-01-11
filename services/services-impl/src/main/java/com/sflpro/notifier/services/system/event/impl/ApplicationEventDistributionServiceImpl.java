package com.sflpro.notifier.services.system.event.impl;

import com.sflpro.notifier.persistence.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import com.sflpro.notifier.services.system.event.model.ApplicationEvent;
import com.sflpro.notifier.services.system.event.model.ApplicationEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 10:18 PM
 */
@Service
public class ApplicationEventDistributionServiceImpl implements ApplicationEventDistributionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationEventDistributionServiceImpl.class);

    /* Constants */
    private static final int MAX_THREAD_POOL_SIZE = 20;

    private static final int CORE_THREAD_POOL_SIZE = 10;

    /* Properties */
    private Collection<ApplicationEventListener> eventListeners;

    private ExecutorService executorService;

    /* Dependencies */
    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    /* Constructors */
    public ApplicationEventDistributionServiceImpl() {
        LOGGER.debug("Initializing application event distribution service");
        this.eventListeners = new CopyOnWriteArrayList<>();
        // Initialize executor service
        initExecutorService();
    }

    @Override
    public void subscribe(@Nonnull final ApplicationEventListener eventListener) {
        Assert.notNull(eventListener, "Event listener should not be null");
        LOGGER.debug("Subscribing event listener - {}", eventListener);
        this.eventListeners.add(eventListener);
    }

    @Override
    public void publishAsynchronousEvent(@Nonnull final ApplicationEvent applicationEvent) {
        assertApplicationEventNotNull(applicationEvent);
        LOGGER.debug("Processing Asynchronous event - {}", applicationEvent);
        this.executorService.submit(new EventListenersAsynchronousIteratorTask(applicationEvent));
    }

    @Override
    public void publishSynchronousEvent(@Nonnull final ApplicationEvent applicationEvent) {
        assertApplicationEventNotNull(applicationEvent);
        LOGGER.debug("Processing Synchronous event - {}", applicationEvent);
        final Runnable runnable = new EventListenersSynchronousIteratorTask(applicationEvent);
        runnable.run();
    }

    /* Utility methods */
    private void assertApplicationEventNotNull(final ApplicationEvent applicationEvent) {
        Assert.notNull(applicationEvent, "Application event should not be null");
    }

    private void initExecutorService() {
        final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
        threadPoolExecutor.setCorePoolSize(CORE_THREAD_POOL_SIZE);
        threadPoolExecutor.setMaximumPoolSize(MAX_THREAD_POOL_SIZE);
        // Publish executor service
        this.executorService = threadPoolExecutor;
    }

    /* Properties getters and setters */
    public PersistenceUtilityService getPersistenceUtilityService() {
        return persistenceUtilityService;
    }

    public void setPersistenceUtilityService(final PersistenceUtilityService persistenceUtilityService) {
        this.persistenceUtilityService = persistenceUtilityService;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(final ExecutorService executorService) {
        this.executorService = executorService;
    }

    /* Inner classes */
    private class EventListenersAsynchronousIteratorTask extends AbstractEventListenersIteratorTask {

        public EventListenersAsynchronousIteratorTask(final ApplicationEvent applicationEvent) {
            super(applicationEvent);
        }

        @Override
        protected void processApplicationEvent(final ApplicationEvent applicationEvent, final ApplicationEventListener eventListener) {
            executorService.submit(new ApplicationEventProcessorTask(applicationEvent, eventListener));
        }
    }

    private class EventListenersSynchronousIteratorTask extends AbstractEventListenersIteratorTask {

        public EventListenersSynchronousIteratorTask(final ApplicationEvent applicationEvent) {
            super(applicationEvent);
        }

        @Override
        protected void processApplicationEvent(final ApplicationEvent applicationEvent, final ApplicationEventListener eventListener) {

            try {
                persistenceUtilityService.runInNewTransaction(() -> {
                    eventListener.process(applicationEvent);
                });
            } catch (final Exception ex) {
                // Swallow and just log
                LOGGER.error("Error occurred while executing event listener", ex);
            }
        }
    }

    private abstract class AbstractEventListenersIteratorTask implements Runnable {

        /* Properties */
        private final ApplicationEvent applicationEvent;

        public AbstractEventListenersIteratorTask(final ApplicationEvent applicationEvent) {
            this.applicationEvent = applicationEvent;
        }

        @Override
        public void run() {
            eventListeners.forEach(eventListener -> {
                if (eventListener.subscribed(applicationEvent)) {
                    processApplicationEvent(applicationEvent, eventListener);
                }
            });
        }

        /* Abstract methods */
        protected abstract void processApplicationEvent(final ApplicationEvent applicationEvent, final ApplicationEventListener eventListener);
    }

    private class ApplicationEventProcessorTask implements Runnable {

        /* Properties */
        private final ApplicationEvent applicationEvent;

        private final ApplicationEventListener eventListener;

        /* Constructors */
        public ApplicationEventProcessorTask(final ApplicationEvent applicationEvent, final ApplicationEventListener eventListener) {
            this.applicationEvent = applicationEvent;
            this.eventListener = eventListener;
        }

        @Override
        public void run() {
            try {
                persistenceUtilityService.runInPersistenceSession(() -> {
                    eventListener.process(applicationEvent);
                });
            } catch (final Exception ex) {
                // Swallow and just log
                LOGGER.error("Error occurred while executing event listener", ex);
            }
        }
    }
}
