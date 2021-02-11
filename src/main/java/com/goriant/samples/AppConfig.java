package com.goriant.samples;

import java.time.LocalTime;
import java.util.Base64;
import java.util.Map;

public class AppConfig {
    private String user;
    private String password;
    private String checkIn;
    private String checkOut;
    private String chatUrl;
    private String chatRequestBody;
    private Map<String, String> chatHeaders;
    private String loginUrl;
    private Map<String, String> loginHeaders;
    private String otpUrl;
    private String telegramToken;
    private String telegramChatId;

    public String getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(String telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public String getTelegramToken() {
        return telegramToken;
    }

    public void setTelegramToken(String telegramToken) {
        this.telegramToken = telegramToken;
    }

    public String getOtpUrl() {
        return otpUrl;
    }

    public void setOtpUrl(String otpUrl) {
        this.otpUrl = otpUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public Map<String, String> getLoginHeaders() {
        return loginHeaders;
    }

    public void setLoginHeaders(Map<String, String> loginHeaders) {
        this.loginHeaders = loginHeaders;
    }

    public String getChatRequestBody() {
        return chatRequestBody;
    }

    public void setChatRequestBody(String chatRequestBody) {
        this.chatRequestBody = chatRequestBody;
    }

    public String getChatUrl() {
        return chatUrl;
    }

    public void setChatUrl(String chatUrl) {
        this.chatUrl = chatUrl;
    }

    public Map<String, String> getChatHeaders() {
        return chatHeaders;
    }

    public void setChatHeaders(Map<String, String> chatHeaders) {
        this.chatHeaders = chatHeaders;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AppConfig{");
        sb.append("user='").append(user).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", checkIn='").append(checkIn).append('\'');
        sb.append(", checkOut='").append(checkOut).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
