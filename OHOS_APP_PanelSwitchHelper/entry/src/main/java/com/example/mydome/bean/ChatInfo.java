package com.example.mydome.bean;

public class ChatInfo {

    public String message;
    public boolean owner;
    public static boolean sBoolean = true;

    public ChatInfo(String message) {
        this.message = message;
    }

    public static ChatInfo CREATE(String message) {
        ChatInfo chatInfo = new ChatInfo(message);
        sBoolean = !sBoolean;
        return chatInfo;
    }

    public String getMessage() {
        return message;
    }

    public boolean isOwner() {
        return owner;
    }

    public static boolean issBoolean() {
        return sBoolean;
    }
}
