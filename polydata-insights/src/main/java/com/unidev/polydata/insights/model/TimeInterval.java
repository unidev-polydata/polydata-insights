package com.unidev.polydata.insights.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Time interval used for queries
 */
public enum TimeInterval {

    DAY(TimeUnit.DAYS.toMillis(1)),
    WEEK(TimeUnit.DAYS.toMillis(7)),
    MONTH(TimeUnit.DAYS.toMillis(30)),
    YEAR(TimeUnit.DAYS.toMillis(365)),
    ALL(null);

    public Date fetchDateFrom(Date date) {
        if (interval == null) {
            return new Date(0);
        }
        return new Date(date.getTime() - interval);
    }

    private Long interval;

    TimeInterval(Long interval) {
        this.interval = interval;
    }

    public Long getInterval() {
        return interval;
    }

}
