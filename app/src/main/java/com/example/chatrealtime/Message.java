package com.example.chatrealtime;

public class Message {

    public String key;
    public String name;
    public String text;
    public long time;

    public Message(){}

    public Message(String name, String text){
        this.name = name;
        this.text = text;
        this.time = System.currentTimeMillis();
    }
}