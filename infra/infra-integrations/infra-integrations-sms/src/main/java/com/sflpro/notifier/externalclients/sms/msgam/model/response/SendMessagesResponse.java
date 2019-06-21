package com.sflpro.notifier.externalclients.sms.msgam.model.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 5:09 PM
 */
@XmlRootElement(name = "xml_result")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendMessagesResponse {

    private static final long serialVersionUID = 1234987945123649992L;

    /* Properties */
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "res")
    private String result;

    @XmlAttribute(name = "description")
    private String description;

    @XmlElement(name = "push")
    private Message message;

    @XmlElement(name = "inf_answer")
    private Information information;


    public String getSid() {
        return message.getId().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return result.equals("0");
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public SendMessagesResponse() {
    }

    public SendMessagesResponse(final String name, final String result, final String description, final Message message) {
        this.name = name;
        this.result = result;
        this.description = description;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof SendMessagesResponse)) return false;

        SendMessagesResponse response = (SendMessagesResponse) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(name, response.name)
                .append(result, response.result)
                .append(description, response.description)
                .append(message, response.message)
                .append(information, response.information)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(name)
                .append(result)
                .append(description)
                .append(message)
                .append(information)
                .toHashCode();
    }

    @Override
    public String
    toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("result", result)
                .append("description", description)
                .append("message", message)
                .append("information", information)
                .toString();
    }
}
