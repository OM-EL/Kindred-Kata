package com.kindredgroup.unibetlivetest.types;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExceptionType {

    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND),
    SELECTION_NOT_FOUND(HttpStatus.NOT_FOUND),
    SELECTIONS_NOT_FOUND(HttpStatus.NO_CONTENT),
    DUPLICATE_BET_EXCEPTION(HttpStatus.CONFLICT),
    ODD_CHANGED(601),
    INSUFFICIENT_BALANCE(600),
    SELECTION_CLOSED(602);

    private final int statusCode;
    private final boolean isCustom;

    ExceptionType(int statusCode) {
        this.statusCode = statusCode;
        this.isCustom = true;
    }

    ExceptionType(HttpStatus status) {
        this.statusCode = status.value();
        this.isCustom = false;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public boolean isCustom() {
        return isCustom;
    }
}
