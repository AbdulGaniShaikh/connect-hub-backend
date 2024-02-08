package com.shaikhabdulgani.ConnectHub.util.enums;

public enum MessageType {

    TEXT,
    POST;

    public boolean isText(){
        return this==TEXT;
    }

    public boolean isPost(){
        return this==POST;
    }
}
