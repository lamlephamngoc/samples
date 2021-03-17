package com.goriant.samples.service;

import com.goriant.samples.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckInService {
    private static final Logger log = LoggerFactory.getLogger(CheckInService.class);

    private static final CheckInService checkInService = new CheckInService();

    public static CheckInService getInstance() {
        return checkInService;
    }

    public void checkIn(AppConfig config) throws Exception {

        LoginService loginService = LoginService.getInstance();
        log.info("Get verification token");
        String verificationToken = loginService.getVerificationToken(config);

        log.info("Submit login form with verification token");
        loginService.login(config, verificationToken);

        String otp = OtpCollector.getInstance().getOtp(config);

        log.info("Submit OTP");
        loginService.submitOtp(config, otp);
    }
}
