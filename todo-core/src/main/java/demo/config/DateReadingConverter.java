package demo.config;

import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Created by user on 7/8/17.
 */
public class DateReadingConverter implements Converter<Long, Date> {
    @Override
    public Date convert(Long aLong) {
        return new Date(aLong);
    }
}
