package com.kindredgroup.unibetlivetest.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing an exception.
 * This class captures details of an exception for API responses.
 */
@Data
@Accessors(chain = true)
public class ExceptionDto {

    private String path;           // The API path where the exception occurred
    private String errormessage;   // Detailed error message
    private String timestamp = LocalDateTime.now().toString();  // Timestamp of when the exception was captured

    // No custom constructor is needed here as the default one is sufficient
    // The timestamp is initialized directly in its declaration
}
