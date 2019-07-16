package com.sflpro.notifier.externalclients.sms.nikitamobile;

import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NikitamobileDateTimeUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public static LocalDateTime parse(final String strVal) {
        Assert.hasText(strVal,"Null or empty text was passed as an argument for parameter 'strVal'.");
        return LocalDateTime.parse(strVal, FORMATTER);
    }

    public static String format(final LocalDateTime localDateTime) {
        Assert.notNull(localDateTime,"Null was passed as an argument for parameter 'localDateTime'.");
        return FORMATTER.format(localDateTime);
    }
}
