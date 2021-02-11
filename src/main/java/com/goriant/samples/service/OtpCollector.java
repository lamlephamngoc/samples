package com.goriant.samples.service;

import com.goriant.samples.AppConfig;
import com.goriant.samples.Constants;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class OtpCollector {
    private static final Logger log = LoggerFactory.getLogger(OtpCollector.class);
    private static final Pattern otpPattern = Pattern.compile(Constants.OTP_REGEX);
    private static TelegramBot telegramBot;

    private static final OtpCollector OTP_COLLECTOR = new OtpCollector();

    public static OtpCollector getInstance() {
        return OTP_COLLECTOR;
    }

    public String getOtp(AppConfig config) throws UnirestException {
        HttpRequestWithBody otpRequest = Unirest.post(config.getChatUrl());
        otpRequest.headers(config.getChatHeaders());
        otpRequest.body(config.getChatRequestBody());
        HttpResponse<String> otpResponse = otpRequest.asString();
        String otp = extractOtp(otpResponse.getBody());

        String msg = String.format("[%s] %s OTP is: %s", LocalDateTime.now().withNano(0).format(ISO_LOCAL_DATE_TIME), config.getUser(), otp);
        getTelegramBot(config.getTelegramToken())
                .execute(new SendMessage(config.getTelegramChatId(), msg));
        log.info("OTP : {}", otp);
        return otp;
    }

    private TelegramBot getTelegramBot(String teleToken) {
        if (null == telegramBot) return new TelegramBot(teleToken);
        return telegramBot;
    }

    private String extractOtp(String body) {
        Matcher matcher = otpPattern.matcher(body);
        if (matcher.find())
            return matcher.group(0).replace("\"", "").replace("\\", "");
        return null;
    }
}
