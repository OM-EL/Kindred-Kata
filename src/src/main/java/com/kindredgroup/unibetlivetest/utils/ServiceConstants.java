package com.kindredgroup.unibetlivetest.utils;

import org.springframework.http.HttpStatus;
public final class ServiceConstants {

    // Success messages
    public static final String OK_MESSAGE = "OK - Pari enregistré";
    public static final String FETCHED_EVENTS_SUCCESS_MESSAGE = "Fetched events successfully.";
    public static final String FETCHED_SELECTIONS_SUCCESS_MESSAGE = "Fetched selections successfully.";

    // Log message templates
    public static final String LOG_MESSAGE_TEMPLATE = "{} bet(s) closed in {} ms.";
    public static final String UPDATE_LOG_MESSAGE_TEMPLATE = "{} selection(s) updated randomly.";
    public static final String CLOSE_LOG_MESSAGE_TEMPLATE = "{} selection(s) closed randomly.";
    public static final String NO_EVENTS_FOUND_LOG_TEMPLATE = "No events found for live state: {}";

    // Error and info messages and templates
    public static final String ODD_CHANGED_MESSAGE = "Odds change";
    public static final String SELECTION_CLOSED_MESSAGE = "Selection closed";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";
    public static final String DUPLICATE_BET_MESSAGE = "Conflict, bet already in progress";
    public static final String BAD_REQUEST_MESSAGE_TEMPLATE = "bad request : %s";
    public static final String SELECTION_NOT_FOUND_TEMPLATE = "Selection with id: %s not found";
    public static final String CUSTOMER_NOT_FOUND_TEMPLATE = "Customer with id: %s not found";
    public static final String ERROR_PROCESSING_BET_TEMPLATE = "Error processing bet with ID: %s";
    public static final String NO_EVENTS_FOUND_EXCEPTION_MESSAGE = "No events found";

    // HttpStatus constants
    public static final HttpStatus BAD_REQUEST_STATUS = HttpStatus.BAD_REQUEST;
    public static final HttpStatus INTERNAL_SERVER_ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    // Config related constants
    public static final String BATCH_EXECUTION_INTERVAL_PROP = "${batch.execution.interval:5000}";

    // Private constructor to prevent instantiation
    private ServiceConstants() {
        throw new UnsupportedOperationException("ServiceConstants class cannot be instantiated.");
    }
}
