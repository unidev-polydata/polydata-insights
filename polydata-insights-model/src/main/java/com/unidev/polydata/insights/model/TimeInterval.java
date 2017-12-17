package com.unidev.polydata.insights.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Time interval used for queries
 */
public enum TimeInterval {

    HOUR_1(TimeUnit.HOURS.toMillis(1)),
    HOURS_2(TimeUnit.HOURS.toMillis(2)),
    HOURS_4(TimeUnit.HOURS.toMillis(4)),
    HOURS_6(TimeUnit.HOURS.toMillis(6)),
    HOURS_12(TimeUnit.HOURS.toMillis(12)),
    DAY(TimeUnit.DAYS.toMillis(1)),
    WEEK(TimeUnit.DAYS.toMillis(7)),
    MONTH(TimeUnit.DAYS.toMillis(30)),
    YEAR(TimeUnit.DAYS.toMillis(365)),
    ALL(null);

    private Long interval;

    TimeInterval(Long interval) {
        this.interval = interval;
    }

    public Date fetchDateFrom(Date date) {
        if (interval == null) {
            return new Date(0);
        }
        return new Date(date.getTime() - interval);
    }

    public Long getInterval() {
        return interval;
    }

}
