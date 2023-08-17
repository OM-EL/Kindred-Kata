package com.kindredgroup.unibetlivetest.exception;

import com.kindredgroup.unibetlivetest.dto.ExceptionDto;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.utils.ServiceConstants;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * Global exception handling for the application.
 */
@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class ExceptionHttpTranslator {

    /**
     * Handles custom business exceptions.
     *
     * @param request HTTP request causing the exception.
     * @param e The CustomException thrown.
     * @return Response entity with details of the exception.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionDto> businessException(HttpServletRequest request, final CustomException e) {
        ExceptionDto dto = new ExceptionDto()
                .setErrormessage(e.getMessage())
                .setPath(request.getServletPath());

        return buildResponseEntity(dto, e.getException());
    }

    private ResponseEntity<ExceptionDto> buildResponseEntity(ExceptionDto dto, ExceptionType exceptionType) {
        if (exceptionType.isCustom()) {
            return ResponseEntity.status(exceptionType.getStatusCode()).body(dto);
        } else {
            return new ResponseEntity<>(dto, HttpStatus.valueOf(exceptionType.getStatusCode()));
        }
    }

    /**
     * Handles invalid input exceptions.
     *
     * @param request HTTP request causing the exception.
     * @param ex The exception thrown.
     * @return Response entity with details of the exception.
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            ConversionFailedException.class
    })
    public ResponseEntity<ExceptionDto> handleBadRequestException(HttpServletRequest request, Exception ex) {
        ExceptionDto dto = new ExceptionDto()
                .setErrormessage(String.format(ServiceConstants.BAD_REQUEST_MESSAGE_TEMPLATE, ex.getMessage()))
                .setPath(request.getContextPath());
        return new ResponseEntity<>(dto, ServiceConstants.BAD_REQUEST_STATUS);
    }


    /**
     * Catches all unhandled exceptions across the application.
     *
     * @param request HTTP request causing the exception.
     * @param ex The unhandled exception.
     * @return Response entity with details of the exception.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> globalExceptionHandler(HttpServletRequest request, Exception ex) {
        ExceptionDto dto = new ExceptionDto()
                .setErrormessage(ex.getMessage())
                .setPath(request.getContextPath());
        return new ResponseEntity<>(dto, ServiceConstants.INTERNAL_SERVER_ERROR_STATUS);
    }
}
