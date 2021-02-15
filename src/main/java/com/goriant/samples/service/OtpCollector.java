package com.goriant.samples.service;

import com.goriant.samples.AppConfig;
import com.goriant.samples.Constants;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpCollector {
    private static final Logger log = LoggerFactory.getLogger(OtpCollector.class);
    private static final Pattern otpPattern = Pattern.compile(Constants.OTP_REGEX);

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
        log.info("OTP : {}", otp);
        return otp;
    }

    private String extractOtp(String body) {
        Matcher matcher = otpPattern.matcher(body);
        if (matcher.find())
            return matcher.group(0).replace("\"", "").replace("\\", "");
        return null;
    }
}
