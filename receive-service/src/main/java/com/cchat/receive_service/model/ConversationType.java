package com.cchat.receive_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ConversationType {
    DM,
    GROUP;

    public boolean isDm()    { return this == DM; }
    public boolean isGroup() { return this == GROUP; }

    @JsonCreator
    public static ConversationType from(String value) {
        if (value == null) return null;
        return ConversationType.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
