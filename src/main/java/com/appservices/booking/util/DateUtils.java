package com.appservices.booking.util;

import com.appservices.booking.exception.BookingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public static LocalTime get24HourTime(Integer hours, Integer minutes) {
        return LocalTime.now()
                .withHour(hours)
                .withMinute(minutes)
                .withSecond(0)
                .withNano(0);
    }

    public static LocalTime get24HourTime(String time) {
        LocalTime parse;
        try {
            parse = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            logger.atInfo().log("Parsed time string: "+ parse);
        } catch (Exception e) {
            throw new BookingException("Time conversion error.");
        }
        return parse;
    }
}
