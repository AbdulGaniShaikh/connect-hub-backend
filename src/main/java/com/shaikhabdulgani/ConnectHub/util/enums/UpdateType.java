package com.shaikhabdulgani.ConnectHub.util.enums;

public enum UpdateType {

    DESCRIPTION,
    PROFILE,
    COVER;

    public boolean isDescription(){
        return this==DESCRIPTION;
    }

    public boolean isProfile(){
        return this==PROFILE;
    }
    public boolean isCover(){
        return this==COVER;
    }

}
