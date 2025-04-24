package com.eighttoten.member.domain;

public enum Mode {
    SPICY("매운맛"),MILD("순한맛");

    private final String label;

    Mode(String label) {
        this.label = label;
    }
}