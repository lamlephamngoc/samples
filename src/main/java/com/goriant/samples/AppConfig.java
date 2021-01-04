package com.goriant.samples;

import java.time.LocalTime;
import java.util.Base64;

public class AppConfig {
    private String user;
    private String password;
    private String sessionId;
    private String otpUrl;
    private String checkIn;
    private String checkOut;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return new String(Base64.getDecoder().decode(password));
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOtpUrl() {
        return otpUrl;
    }

    public void setOtpUrl(String otpUrl) {
        this.otpUrl = otpUrl;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AppConfig{");
        sb.append("user='").append(user).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", sessionId='").append(sessionId).append('\'');
        sb.append(", otpUrl='").append(otpUrl).append('\'');
        sb.append(", checkIn='").append(checkIn).append('\'');
        sb.append(", checkOut='").append(checkOut).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public LocalTime getRandomCheckIn() {
        return timeRandomFrom(this.checkIn);
    }

    public LocalTime getRandomCheckOut() {
        return timeRandomFrom(this.checkOut);
    }

    public LocalTime timeRandomFrom(String input) {
        String[] ranges = input.split("-");
        int hourFrom = Integer.parseInt(ranges[0].split(":")[0]);
        int minuteFrom = Integer.parseInt(ranges[0].split(":")[1]);

        int hourTo = Integer.parseInt(ranges[1].split(":")[0]);
        int minuteTo = Integer.parseInt(ranges[1].split(":")[1]);

        return TimeUtils.between(LocalTime.of(hourFrom, minuteFrom), LocalTime.of(hourTo, minuteTo));
    }
}
