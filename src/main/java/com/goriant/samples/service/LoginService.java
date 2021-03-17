package com.goriant.samples.service;

import com.goriant.samples.config.AppConfig;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private static final LoginService loginService = new LoginService();
    private static TelegramBot telegramBot;

    public static LoginService getInstance() {
        return loginService;
    }

    public String getVerificationToken(AppConfig config) throws IOException {
        Document doc = Jsoup.connect(config.getLoginUrl())
                .headers(config.getLoginHeaders())
                .get();
        Element element = doc.selectFirst("input[name=__RequestVerificationToken]");

        String requestVerificationTokenValue = element.val();
        log.info("Verification Token : {}", requestVerificationTokenValue);

        return requestVerificationTokenValue;
    }

    public void login(AppConfig config, String verificationToken) throws UnirestException {
        String body = String.format("UserName=%s&Password=%s&__RequestVerificationToken=%s", config.getUser(), config.getPassword(), verificationToken);

        log.info("Request body: {}", body.replace(config.getPassword(), "******"));

        HttpResponse<String> response = Unirest.post(config.getLoginUrl())
                .headers(config.getLoginHeaders())
                .body(body)
                .asString();

        log.info(response.getBody());
    }

    public void submitOtp(AppConfig config, String otp) throws UnirestException {
        String body = String.format("emailAddress=%s&otp=%s", config.getUser(), otp);
        log.info(body);

        HttpResponse<String> response = Unirest.post(config.getOtpUrl())
                .headers(config.getLoginHeaders())
                .body(body)
                .asString();
        String res = response.getBody();
        log.info("Submit OTP status : {}", res);

        String msg = buildTelegramMsg(config, otp, res);
        log.info("Call telegram chat bot with msg : {}", msg);

        getTelegramBot(config.getTelegramToken())
                .execute(new SendMessage(config.getTelegramChatId(), msg));
    }

    private String buildTelegramMsg(AppConfig config, String otp, String res) {
        return String.format("[%s] %s OTP is: %s - with HRM response %s",
                LocalDateTime.now().withNano(0).format(ISO_LOCAL_DATE_TIME),
                config.getUser(), otp, res);
    }

    private static TelegramBot getTelegramBot(String teleToken) {
        if (null == telegramBot) {
            telegramBot = new TelegramBot(teleToken);
        }
        return telegramBot;
    }
}
