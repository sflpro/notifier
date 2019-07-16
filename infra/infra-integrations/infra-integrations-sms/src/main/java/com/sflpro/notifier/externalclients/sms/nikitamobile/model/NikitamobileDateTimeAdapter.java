package com.sflpro.notifier.externalclients.sms.nikitamobile.model;

import com.sflpro.notifier.externalclients.sms.nikitamobile.NikitamobileDateTimeUtil;
import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:16 AM
 */
public class NikitamobileDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {


    @Override
    public LocalDateTime unmarshal(final String strVal) {
        return StringUtils.isEmpty(strVal) ? null : NikitamobileDateTimeUtil.parse(strVal);
    }

    @Override
    public String marshal(final LocalDateTime localDateTime) {
        return  localDateTime == null ? null : NikitamobileDateTimeUtil.format(localDateTime);
    }
}
