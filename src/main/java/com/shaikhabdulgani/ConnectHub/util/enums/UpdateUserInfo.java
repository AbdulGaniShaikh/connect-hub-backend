package com.shaikhabdulgani.ConnectHub.util.enums;

public enum UpdateUserInfo {

    DESCRIPTION,
    PROFILE_IMAGE,
    COVER_IMAGE;

    public boolean isDescription() {
        return this == DESCRIPTION;
    }

    public boolean isProfileImage() {
        return this == PROFILE_IMAGE;
    }

    public boolean isCoverImage() {
        return this == COVER_IMAGE;
    }
}
