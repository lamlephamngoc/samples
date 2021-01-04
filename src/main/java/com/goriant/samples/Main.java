package com.goriant.samples;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.goriant.samples.Constants.CONFIG_PATH;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static boolean shouldCheckIn;
    private static boolean shouldCheckOut;
    private static final Set<String> checkInDays = new HashSet<>();

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
            if (passDay() && (shouldCheckIn == Boolean.FALSE || shouldCheckOut == Boolean.FALSE)) {

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
                samples(config);
                shouldCheckIn = Boolean.FALSE;
                log.info("End Check-in");
            }

            if (shouldCheckOut && meetCheckOut(randomCheckOut)) {
                log.info("Start CheckOut");
                samples(config);
                shouldCheckOut = Boolean.FALSE;
                log.info("End Check-out");
            }

            Thread.sleep(60_000);
        }
    }

    private static boolean meetCheckOut(LocalTime checkOut) {
        return LocalTime.now().compareTo(checkOut) > 0;
    }

    private static boolean meetCheckIn(LocalTime checkIn) {
        return LocalTime.now().compareTo(checkIn) > 0;
    }

    private static boolean passDay() {

        return LocalTime.now().compareTo(LocalTime.of(23, 59)) > 0;
    }

    private static void samples(AppConfig config) throws MalformedURLException, InterruptedException {
        RemoteWebDriver driver = RemoteWebDriverFactory.createDriverFromSession("", Constants.SELENIUM_SERVER);
        driver.get(Constants.HRM_URL);

        WebElement userName = driver.findElementByName(Constants.USER_NAME);
        userName.sendKeys(config.getUser());
        Thread.sleep(1000);

        WebElement pass = driver.findElementByName(Constants.PASSWORD);
        pass.sendKeys(config.getPassword());
        pass.sendKeys(Keys.RETURN);
        Thread.sleep(1000);

        String otp = getOtp(config);
        log.info("Login with OTP : {}", otp);

        WebElement otpInput = driver.findElementById(Constants.OTP_ID);
        otpInput.sendKeys(otp);
        otpInput.sendKeys(Keys.RETURN);

        Thread.sleep(5_000);
        driver.quit();
    }

    private static String getOtp(AppConfig config) throws MalformedURLException, InterruptedException {
        RemoteWebDriver driver = RemoteWebDriverFactory.createDriverFromSession(config.getSessionId(), Constants.SELENIUM_SERVER);

        driver.get(config.getOtpUrl());

        Thread.sleep(5000);
        List<WebElement> chats = driver.findElements(By.className(Constants.CHAT_CSS_SELECTOR));
        WebElement otpMsg = chats.get(chats.size() - 1);
        String[] msgArr = otpMsg.getText().split(" ");
        return msgArr[msgArr.length - 1]; // otp
    }
}
