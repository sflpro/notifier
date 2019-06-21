package com.sflpro.notifier.externalclients.sms.msgam.model.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 5:48 PM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    @XmlAttribute(name = "lgn")
    private String login;

    @XmlAttribute(name = "pwd")
    private String pass;

    public User(final String senderLogin, final String senderPass) {
        this.login = senderLogin;
        this.pass = senderPass;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User user = (User) o;
        return new EqualsBuilder()
                .append(login, user.login)
                .append(pass, user.pass)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(login)
                .append(pass)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("login", login)
                .append("pass", pass)
                .toString();
    }
}
