package com.kindredgroup.unibetlivetest.types;

public class CustomHttpStatus {
    private final int value;
    private final String reasonPhrase;

    public CustomHttpStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
