package com.kindredgroup.unibetlivetest.dto;

import lombok.Data;

/**
 * Represents a generic API response format.
 * This class encapsulates the returned data, a message, potential errors, and the HTTP status code.
 */
@Data
public class ApiResponse {
    private Object data;       // The actual response data
    private String message;    // A human-readable message, typically for successes
    private String error;      // A human-readable error message, if applicable
    private int statusCode;    // The HTTP status code of the response
}

