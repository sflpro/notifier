package com.sflpro.notifier.externalclients.email.mandrill.exception;

import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
public class MandrillMessageRejectedException extends MandrillEmailClientRuntimeException {

    private static final long serialVersionUID = -8375948228824433272L;
    private final transient MandrillMessageStatus mandrillMessageStatus;

    public MandrillMessageRejectedException(MandrillMessageStatus mandrillMessageStatus) {
        super("Email rejected");
        this.mandrillMessageStatus = mandrillMessageStatus;
    }

    public MandrillMessageStatus getMandrillMessageStatus() {
        return mandrillMessageStatus;
    }
}
