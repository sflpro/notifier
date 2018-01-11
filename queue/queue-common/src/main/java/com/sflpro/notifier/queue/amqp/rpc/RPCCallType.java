package com.sflpro.notifier.queue.amqp.rpc;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 11:54 PM
 */
public enum RPCCallType {
    CAR_WASH_STATION_UPDATED("carwashstation.updated"),
    START_PAYMENT_METHOD_AUTHORIZATION_PROCESSING("paymentmethodauthorization.processing.start"),
    START_PAYMENT_METHOD_REMOVAL_PROCESSING("paymentmethodremoval.processing.start"),
    START_ORDER_PAYMENT_REQUEST_PROCESSING("orderpaymentrequest.processing.start"),
    LOYALTY_POINTS_REWARDED("loyalty.rewarded"),
    EMAIL_CAR_WASH_EMPLOYEE_PASSWORD_UPDATED("carwashemployee.password.updated"),
    EMAIL_CAR_WASH_EMPLOYEE_CREATED("carwashemployee.created"),
    EMAIL_CAR_WASH_MANAGER_CREATED("carwashmanager.created"),
    START_REWARDED_VOUCHERS_GENERATION("voucher.rewarded.generation.start"),
    REWARDED_VOUCHER_GENERATION_COMPLETED("voucher.rewarded.generation.completed"),
    START_USER_IDENTITY_AUTHORIZATION_PROCESSING("useridentityauthorization.processing.start"),
    EMAIL_CAR_WASH_APPOINTMENT_CANCELLED("carwashappointment.cancelled"),
    EMAIL_CAR_WASH_APPOINTMENT_FULFILLED("carwashappointment.fulfilled"),
    START_NOTIFICATION_PROCESSING("notification.processing.start"),
    START_PUSH_NOTIFICATION_SUBSCRIPTION_PROCESSING("notification.push.subscription.processing.start"),
    START_PAYMENT_PROVIDER_NOTIFICATION_REQUEST_PROCESSING("payment.provider.notification.request.processing.start"),
    START_PAYMENT_PROVIDER_REDIRECT_RESULT_PROCESSING("payment.provider.redirect.result.processing.start"),

    /* Email */
    EMAIL_CAR_WASH_COMPANY_STATUS_UPDATED("email.carwashcompany.status.updated"),
    EMAIL_SUPPORT_TICKET_OPENED("email.supportticket.opened"),
    EMAIL_CAR_WASH_STATION_APPROVAL_STATUS_CHANGED("email.carwashstation.approvalstatus.changed"),
    EMAIL_VERIFICATION_TOKEN_CREATED("email.verificationtoken.created"),
    EMAIL_CAR_WASH_COMPANY_REPORT_CREATED("email.carwashcompany.report.created"),

    EMAIL_EXTERNAL_INTEGRATION_USER_CREATED("externalintegrationuser.created"),
    EMAIL_EXTERNAL_INTEGRATION_USER_REGISTERED("externalintegrationuser.registered"),

    /* Order */
    EMAIL_ORDER_POST_PAYED("email.order.pospayed"),
    EMAIL_ORDER_PAYMENT_REMINDER("email.order.pospayed.reminder"),

    /* Business */
    EMAIL_BUSINESS_MANAGER_PASSWORD_UPDATED("businessmanager.password.updated"),
    EMAIL_BUSINESS_MANAGER_CREATED("businessmanager.created"),
    EMAIL_BUSINESS_COMPANY_ACTIVATED("businesscompany.activated"),

    /* Retention reminder */
    EMAIL_RETENTION_REMINDER_CREATED("retention.reminder.created"),

    /* Push notifications */
    PUSH_STATION_REVIEW("push.stationreview"),
    PUSH_ORDER_PAYMENT("push.orderpayment"),
    PUSH_ORDER_PAYMENT_REMINDER("push.orderpayment.reminder"),
    PUSH_RETENTION_REMINDER_CREATED("push.retention.reminder.created");

    /* Properties */
    private final String callIdentifier;

    RPCCallType(final String callIdentifier) {
        this.callIdentifier = callIdentifier;
    }

    /* Properties getters and setters */

    public String getCallIdentifier() {
        return callIdentifier;
    }
}
