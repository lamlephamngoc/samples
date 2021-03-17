package com.goriant.samples.config;

import java.util.HashMap;
import java.util.Map;

public class ChatConfig {

    private String chatUrl;
    private String chatBody;
    private Map<String, String> chatHeaders;

    ChatConfig(String chatStr) {
        String[] chatStrArr = chatStr.split("\n");
        this.chatUrl = chatStrArr[0].substring(8, chatStrArr[0].length() - 3);
        this.chatBody = chatStrArr[15].substring(16, chatStrArr[15].length() - 3);
        this.chatHeaders = getHeader(chatStrArr);
    }

    private Map<String, String> getHeader(String[] chatStrArr) {
        Map<String, String> headerMap = new HashMap<>();
        for (int i = 1; i < chatStrArr.length - 2; i++) {
            String headerStr = chatStrArr[i].substring(6, chatStrArr[i].length() - 3);
            String[] headerArr = headerStr.split(":");
            headerMap.put(headerArr[0], headerArr[1]);
        }

        return headerMap;

    }

    public String getChatUrl() {
        return chatUrl;
    }

    public String getChatBody() {
        return chatBody;
    }

    public Map<String, String> getChatHeaders() {
        return chatHeaders;
    }
}
