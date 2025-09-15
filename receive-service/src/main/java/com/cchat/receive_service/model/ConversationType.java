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
        if (value == null) throw new IllegalArgumentException("ConversationType is null");
        String v = value.trim().toUpperCase();
        switch (v) {
            case "DM":
                return DM;
            case "GROUP":
                return GROUP;
            default:
                throw new IllegalArgumentException("Unknown ConversationType: " + value);
        }
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
