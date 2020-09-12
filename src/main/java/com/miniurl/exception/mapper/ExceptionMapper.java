package com.miniurl.exception.mapper;

import com.miniurl.exception.*;
import com.miniurl.exception.enums.ApiErrorCode;
import com.miniurl.exception.enums.EntityErrorCode;
import com.miniurl.exception.enums.ErrorCode;
import com.miniurl.model.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionMapper  extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handle(Exception e, WebRequest request) {

        System.out.println("Generic Exception : {}"+ e.getMessage());
        e.printStackTrace();

        ApiResponse resp = new ApiResponse(false, ApiErrorCode.INTERNAL_SERVER_ERROR, "something went wrong on our end");
        return handleExceptionInternal(e, resp,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {IllegalArgException.class})
    protected ResponseEntity<Object> handle(IllegalArgException e, WebRequest request) {

        ApiResponse resp = new ApiResponse(false, e.getError() != null ? e.getError() : ApiErrorCode.BAD_REQUEST, e.getMessage() == null ? "that was a bad request" : e.getMessage());
        return handleExceptionInternal(e, resp,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {EntityException.class})
    protected ResponseEntity<Object> handle(EntityException e, WebRequest request) {

        ApiResponse resp = new ApiResponse(false, e.getError() != null ? e.getError() : ApiErrorCode.INTERNAL_SERVER_ERROR, e.getMessage() == null ? "something went wrong on our end" : e.getMessage());
        return handleExceptionInternal(e, resp,
                new HttpHeaders(), getStatus(e.getError()), request);
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    protected ResponseEntity<Object> handle(ForbiddenException e, WebRequest request) {

        ApiResponse resp = new ApiResponse(false, ApiErrorCode.FORBIDDEN_REQUEST, e.getMessage() == null ? "you are not authorized for this action" : e.getMessage());
        return handleExceptionInternal(e, resp,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handle(NotFoundException e, WebRequest request) {

        ApiResponse resp = new ApiResponse(false, EntityErrorCode.NOT_FOUND, e.getMessage() != null ? e.getMessage() : "the request resource was not found on server");
        return handleExceptionInternal(e, resp,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    protected ResponseEntity<Object> handle(UnauthorizedException e, WebRequest request) {

        ApiResponse resp = new ApiResponse(false, e.getError() != null ? e.getError() : ApiErrorCode.UNAUTHORIZED_REQUEST, e.getMessage() == null ? "you are not authorized for this request" : e.getMessage());
        return handleExceptionInternal(e, resp,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    private HttpStatus getStatus(ErrorCode errorCode) {

        if (errorCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        if (errorCode instanceof EntityErrorCode) {
            EntityErrorCode entityErrorCode = (EntityErrorCode) errorCode;

            switch (entityErrorCode) {
                case ALREADY_EXISTS:
                case ALREADY_DELETED:
                case ALREADY_DISABLED:
                    return HttpStatus.BAD_REQUEST;
                case NOT_FOUND:
                    return HttpStatus.NOT_FOUND;
                case CREATE_FAILED:
                case DELETE_FAILED:
                case UPDATE_FAILED:
                case DISABLED_FAILED:
                default:
                    return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
