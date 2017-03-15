package com.soprasteria.initiatives.commons.error.exception;

import java.util.Arrays;

/**
 * Technical exception codes
 *
 * @author jntakpe
 */
public enum TechnicalCode implements ExceptionCode {

    API_BAD_REQUEST(400),
    API_UNAUTHORIZED(401),
    API_FORBIDDEN(403),
    API_NOT_FOUND(404),
    API_ERROR_INTERNAL(500),
    API_UNKNOWN_STATUS(500),
    ILLEGAL_STATE(500);

    private final int status;

    TechnicalCode(int status) {
        this.status = status;
    }

    public static TechnicalCode fromStatus(int status) {
        return Arrays.stream(TechnicalCode.values())
                .filter(t -> t.getStatus() == status)
                .findFirst()
                .orElse(TechnicalCode.API_UNKNOWN_STATUS);
    }

    @Override
    public int getStatus() {
        return status;
    }
}
