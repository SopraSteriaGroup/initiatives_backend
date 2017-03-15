package com.soprasteria.initiatives.commons.error.exception;

/**
 * Business errors code
 *
 * @author jntakpe
 */
public enum BusinessCode implements ExceptionCode {

    ACCOUNT_WRONG_OWNER(403),
    ACCOUNT_NOT_FOUND(404),
    INVALID_PARAMETER(400);

    private final int status;

    BusinessCode(int status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStatus() {
        return status;
    }
}
