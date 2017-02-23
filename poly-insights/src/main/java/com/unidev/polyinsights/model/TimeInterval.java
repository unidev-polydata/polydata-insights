package com.unidev.polyinsights.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Time interval used for queries
 */
public enum TimeInterval {

    DAY(TimeUnit.DAYS.toMillis(1)), WEEK(TimeUnit.DAYS.toMillis(7)), MONTH(TimeUnit.DAYS.toMillis(30)), YEAR(TimeUnit.DAYS.toMillis(365));

    public Date fetchDateFrom(Date date) {
        return new Date(date.getTime() - interval);
    }

    private long interval;

    TimeInterval(long interval) {
        this.interval = interval;
    }

    public long getInterval() {
        return interval;
    }

}
