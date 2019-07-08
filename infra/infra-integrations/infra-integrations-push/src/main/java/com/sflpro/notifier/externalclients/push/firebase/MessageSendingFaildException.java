package com.sflpro.notifier.externalclients.push.firebase;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/5/19
 * Time: 3:43 PM
 */
final class MessageSendingFaildException extends RuntimeException {

    MessageSendingFaildException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
