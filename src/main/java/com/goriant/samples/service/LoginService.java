package com.goriant.samples.service;

import com.goriant.samples.AppConfig;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private static final LoginService loginService = new LoginService();

    public static LoginService getInstance() {
        return loginService;
    }

    public String getVerificationToken(AppConfig config) throws Exception {
        Document doc = Jsoup.connect(config.getLoginUrl())
                .headers(config.getLoginHeaders())
                .get();
        Element element = doc.selectFirst("input[name=__RequestVerificationToken]");

        log.info("Verification Token : {}", element.val());

        return element.val();
    }

    public void login(AppConfig config, String verificationToken) throws Exception {
        String body = String.format("UserName=%s&Password=%s&__RequestVerificationToken=%s", config.getUser(), config.getPassword(), verificationToken);

        log.info("Request body: {}", body.replace(config.getPassword(), "******"));

        HttpResponse<String> response = Unirest.post(config.getLoginUrl())
                .headers(config.getLoginHeaders())
                .body(body)
                .asString();

        log.info(response.getBody());
    }

    public void submitOtp(AppConfig config, String otp) throws Exception {
        String body = String.format("emailAddress=%s&otp=%s", config.getUser(), otp);
        log.info(body);

        HttpResponse<String> response = Unirest.post(config.getOtpUrl())
                .headers(config.getLoginHeaders())
                .body(body)
                .asString();

        log.info("Submit OTP status : {}", response.getBody());
    }
}
