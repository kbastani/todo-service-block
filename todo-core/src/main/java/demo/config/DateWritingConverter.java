package demo.config;

import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class DateWritingConverter implements Converter<Date, Long> {
    @Override
    public Long convert(Date date) {
        return date.getTime();
    }
}
