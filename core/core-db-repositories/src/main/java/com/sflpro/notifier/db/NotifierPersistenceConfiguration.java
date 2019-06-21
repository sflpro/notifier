package com.sflpro.notifier.db;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.sflpro.notifier.db")
@EnableTransactionManagement
@EntityScan(basePackages = "com.sflpro.notifier.db.entities")
@EnableJpaRepositories(basePackages = "com.sflpro.notifier.db.repositories")
@PropertySource(value = "classpath:/com/sflpro/notifier/repositories.properties", ignoreResourceNotFound = true)
public class NotifierPersistenceConfiguration {

}
