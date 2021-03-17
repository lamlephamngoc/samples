package com.goriant.samples.config;

import com.goriant.samples.TimeUtils;

import java.time.LocalTime;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

public class AppConfig {
    private String user;
    private String password;
    private String checkIn;
    private String checkOut;
    private String loginUrl;
    private Map<String, String> loginHeaders;
    private String otpUrl;
    private String telegramToken;
    private String telegramChatId;
    private Set<String> holidays;
    private ChatConfig chatConfig;

    public Set<String> getHolidays() {
        return holidays;
    }

    public void setHolidays(Set<String> holidays) {
        this.holidays = holidays;
    }

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
        return this.chatConfig.getChatBody();
    }

    public String getChatUrl() {
        return this.chatConfig.getChatUrl();
    }

    public Map<String, String> getChatHeaders() {
        return this.chatConfig.getChatHeaders();
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

    public static void main(String[] args) {

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

    private void createChatConfig(String chatStr) {
        this.chatConfig = new ChatConfig(chatStr);
    }

    public static AppConfig from(AppConfig config, String curlChat) {
        config.createChatConfig(curlChat);
        return config;
    }
}
