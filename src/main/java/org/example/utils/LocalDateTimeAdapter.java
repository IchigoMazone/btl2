package org.example.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return (v == null || v.isEmpty()) ? null : LocalDateTime.parse(v, FORMATTER);
    }

    @Override
    public String marshal(LocalDateTime v) throws Exception {
        return (v == null) ? null : v.format(FORMATTER);
    }
}
