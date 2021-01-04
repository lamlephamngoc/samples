package com.goriant.samples;

import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public final class TimeUtils {

    private TimeUtils() {}

    public static LocalTime between(LocalTime startTime, LocalTime endTime) {
        int startSeconds = startTime.toSecondOfDay();
        int endSeconds = endTime.toSecondOfDay();
        int randomTime = ThreadLocalRandom
                .current()
                .nextInt(startSeconds, endSeconds);

        return LocalTime.ofSecondOfDay(randomTime);
    }
}
