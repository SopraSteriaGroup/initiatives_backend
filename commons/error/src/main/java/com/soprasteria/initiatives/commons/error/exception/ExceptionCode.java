package com.soprasteria.initiatives.commons.error.exception;

/**
 * Interface not different exception types
 *
 * @author jntakpe
 */
public interface ExceptionCode {

    /**
     * HTTP status that should be returned by REST API
     *
     * @return HTTP status code
     */
    int getStatus();

}
