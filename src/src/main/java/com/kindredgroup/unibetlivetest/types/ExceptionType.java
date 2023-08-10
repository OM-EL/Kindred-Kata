package com.kindredgroup.unibetlivetest.types;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExceptionType {

    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND),
    EVENT_NOT_FOUND(HttpStatus.NO_CONTENT),
    SELECTION_NOT_FOUND(HttpStatus.NO_CONTENT),

    DUPLICATE_BET_EXCEPTION(HttpStatus.CONFLICT),
    ODD_CHANGED(601),
    INSUFFICIENT_BALANCE(600),
    SELECTION_CLOSED(602);

    @Getter
    private final int statusCode;

    ExceptionType(int statusCode) {
        this.statusCode = statusCode;
    }

    ExceptionType(HttpStatus status) {
        this(status.value());
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(this.statusCode);
    }
}
