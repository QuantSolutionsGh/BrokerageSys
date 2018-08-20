package com.quantsolutions.brokerageapp.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component

public class DateConverter implements Converter<String,LocalDate> {
    @Override
    public LocalDate convert(String source) {
        if (source==null){
            return null;
        }
        return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
