package com.sflpro.notifier.externalclients.sms.nikitamobile.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:16 AM
 */
public class NikitamobileDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    @Override
    public LocalDateTime unmarshal(final String strVal) {

        return LocalDateTime.parse(strVal, formatter);
    }

    @Override
    public String marshal(final LocalDateTime localDateTime) {
        return formatter.format(localDateTime);
    }
}
