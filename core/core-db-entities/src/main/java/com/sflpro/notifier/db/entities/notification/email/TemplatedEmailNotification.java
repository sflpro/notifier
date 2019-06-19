package com.sflpro.notifier.db.entities.notification.email;

import java.util.Map;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 12:07 PM
 */
public interface TemplatedEmailNotification {

    String templateId();
    Map<String,Object> variables();
}
