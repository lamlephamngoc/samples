package com.goriant.samples;

import com.goriant.samples.service.CheckInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static com.goriant.samples.Constants.CONFIG_PATH;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final Set<String> checkInDays = new HashSet<>();
    private static boolean shouldCheckIn = Boolean.TRUE;
    private static boolean shouldCheckOut = Boolean.FALSE;

    public static void main(String[] args) throws Exception {

        log.info(Constants.BREAK_LINE);
        log.info(Constants.BREAK_LINE);
        log.info(Constants.BREAK_LINE);

        log.info("Start samples");

        Yaml yaml = new Yaml(new Constructor(AppConfig.class));

        FileInputStream fileInputStream = new FileInputStream(CONFIG_PATH);
        AppConfig config = yaml.load(fileInputStream);

        log.info("Read app.yaml config : {}}", config);

        LocalTime randomCheckIn = config.getRandomCheckIn();
        LocalTime randomCheckOut = config.getRandomCheckOut();

        log.info("We will CheckIn at `{}` & CheckOut at `{}`", randomCheckIn, randomCheckOut);

        while (true) {

            log.info("Wakeup and run should CheckIn `{}` at `{}` - should CheckOut `{}` at `{}`",
                    shouldCheckIn, randomCheckIn,
                    shouldCheckOut, randomCheckOut);

            // reset checkIn & checkOut
            if (passDayExcludeWeekend() && (shouldCheckIn == Boolean.FALSE || shouldCheckOut == Boolean.FALSE)) {

                String currentDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
                log.info("Passed day - current date `{}` - checkInDays `{}`", currentDate, checkInDays);
                if (checkInDays.add(currentDate)) {
                    shouldCheckIn = Boolean.TRUE;
                    shouldCheckOut = Boolean.TRUE;
                    randomCheckIn = config.getRandomCheckIn();
                    randomCheckOut = config.getRandomCheckOut();

                    log.info("Pass day reset flags and time random for checkIn at `{}` checkOut at `{}`", randomCheckIn, randomCheckOut);
                }
            }

            if (shouldCheckIn && meetCheckIn(randomCheckIn)) {
                log.info("Start CheckIn");
                CheckInService.getInstance().checkIn(config);
                shouldCheckIn = Boolean.FALSE;
                log.info("End Check-in");
            }

            if (shouldCheckOut && meetCheckOut(randomCheckOut)) {
                log.info("Start CheckOut");
                CheckInService.getInstance().checkIn(config);
                shouldCheckOut = Boolean.FALSE;
                log.info("End Check-out");
            }

            Thread.sleep(Constants.ONE_MINUTE);
        }
    }

    private static boolean meetCheckOut(LocalTime checkOut) {
        return LocalTime.now().compareTo(checkOut) > 0;
    }

    private static boolean meetCheckIn(LocalTime checkIn) {
        return LocalTime.now().compareTo(checkIn) > 0;
    }

    private static boolean passDayExcludeWeekend() {
        return LocalTime.now().compareTo(LocalTime.of(3, 9)) > 0
                && (LocalDate.now().getDayOfWeek() != DayOfWeek.SATURDAY || LocalDate.now().getDayOfWeek() != DayOfWeek.SUNDAY);
    }
}
