
package com.np.database.sql.ast.expr;

import java.util.Calendar;
import java.util.Date;

public enum SQLIntervalUnit {
    YEAR,
    YEAR_MONTH,
    QUARTER,
    MONTH,
    WEEK,
    DAY,
    DAY_HOUR,
    DAY_MINUTE,
    DAY_SECOND,
    DAY_MICROSECOND,
    HOUR,
    HOUR_MINUTE,
    HOUR_SECOND,
    HOUR_MICROSECOND,
    MINUTE,
    MINUTE_SECOND,
    MINUTE_MICROSECOND,
    SECOND,
    SECOND_MICROSECOND,
    MICROSECOND,
    DAY_OF_WEEK,
    DOW,
    DAY_OF_MONTH,
    DAY_OF_YEAR,
    YEAR_OF_WEEK,
    YOW,
    TIMEZONE_HOUR,
    TIMEZONE_MINUTE,
    DOY,
    YEAR_TO_MONTH("YEAR TO MONTH")
    ;

    public final String name;
    public final String name_lcase;

    SQLIntervalUnit(String name) {
        this.name = name;
        this.name_lcase = name.toLowerCase();
    }

    SQLIntervalUnit() {
        this.name = name();
        this.name_lcase = name.toLowerCase();
    }

    public static boolean add(Calendar calendar, int intervalInt, SQLIntervalUnit unit) {
        switch (unit) {
            case YEAR:
                calendar.add(Calendar.YEAR, intervalInt);
                return true;
            case MONTH:
                calendar.add(Calendar.MONTH, intervalInt);
                return true;
            case WEEK:
                calendar.add(Calendar.WEEK_OF_MONTH, intervalInt);
                return true;
            case DAY:
                calendar.add(Calendar.DAY_OF_MONTH, intervalInt);
                return true;
            case HOUR:
                calendar.add(Calendar.HOUR_OF_DAY, intervalInt);
                return true;
            case MINUTE:
                calendar.add(Calendar.MINUTE, intervalInt);
                return true;
            case SECOND:
                calendar.add(Calendar.SECOND, intervalInt);
                return true;
            case MICROSECOND:
                calendar.add(Calendar.MILLISECOND, intervalInt);
                return true;
            default:
                return false;
        }
    }

    public boolean isDateTime() {
        switch (this) {
            case HOUR:
            case MINUTE:
            case SECOND:
            case MICROSECOND:
                return true;
            default:
                return false;
        }
    }
}
