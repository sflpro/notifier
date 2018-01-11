package com.sflpro.notifier.externalclients.email.mandrill.exception;

import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
public class MandrillMessageInvalidException extends MandrillEmailClientRuntimeException {

    private static final long serialVersionUID = 6499081323669417175L;
    private final transient MandrillMessageStatus mandrillMessageStatus;

    public MandrillMessageInvalidException(MandrillMessageStatus mandrillMessageStatus) {
        super("Email considered invalid");
        this.mandrillMessageStatus = mandrillMessageStatus;
    }

    public MandrillMessageStatus getMandrillMessageStatus() {
        return mandrillMessageStatus;
    }
}
