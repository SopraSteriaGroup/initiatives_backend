package com.soprasteria.initiatives.commons.error.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Default exception type
 *
 * @author jntakpe
 */
@JsonIgnoreProperties({"stackTrace", "localizedMessage", "suppressed"})
public class DEPException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(DEPException.class);

    private final ExceptionCode exceptionCode;

    private final Map<String, Object> properties = new TreeMap<>();

    public DEPException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public DEPException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public DEPException(Throwable cause, ExceptionCode exceptionCode) {
        super(cause);
        this.exceptionCode = exceptionCode;
    }

    public DEPException(String message, Throwable cause, ExceptionCode exceptionCode) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

    /**
     * Map existing exception to {@link DEPException}
     *
     * @param exception initial exception
     * @return {@link DEPException} wrapping initial exception
     */
    public static DEPException wrap(Throwable exception) {
        return wrap(exception, null);
    }

    /**
     * Map existing exception to {@link DEPException}
     *
     * @param exception     initial exception
     * @param exceptionCode exception code identifying exception
     * @return {@link DEPException} wrapping initial exception
     */
    public static DEPException wrap(Throwable exception, ExceptionCode exceptionCode) {
        if (exception instanceof DEPException) {
            DEPException se = (DEPException) exception;
            if (exceptionCode != null && exceptionCode != se.getExceptionCode()) {
                return new DEPException(exception.getMessage(), exception, exceptionCode);
            }
            return se;
        } else {
            return new DEPException(exception.getMessage(), exception, exceptionCode);
        }
    }


    /**
     * Map existing exception thrown by {@link com.netflix.hystrix.HystrixCommand} (essentially used by Feign)
     *
     * @param exception initial exception
     * @return {@link DEPException} wrapping initial exception
     */
    public static DEPException wrapHystrix(Throwable exception) {
        if (exception instanceof HystrixRuntimeException) {
            HystrixRuntimeException hystrixException = (HystrixRuntimeException) exception;
            Throwable cause = hystrixException.getCause();
            if (cause instanceof FeignException) {
                FeignException feignException = (FeignException) cause;
                return wrap(exception, TechnicalCode.fromStatus(feignException.status()));
            }
        }
        return wrap(exception, TechnicalCode.API_UNKNOWN_STATUS);
    }

    /**
     * Log an exception then throw it
     *
     * @param message   message to log
     * @param exception exception to log and throw
     */
    public static void logThenThrow(String message, Throwable exception) {
        LOGGER.warn(message, exception);
        throw wrapHystrix(exception);
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) properties.get(name);
    }

    public DEPException set(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        printStackTrace(new PrintWriter(s));
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        s.println(this);
        s.println("\t-------------------------------");
        if (exceptionCode != null) {
            s.println("\t" + exceptionCode + ":" + exceptionCode.getClass().getName());
        }
        for (String key : properties.keySet()) {
            s.println("\t" + key + "=[" + properties.get(key) + "]");
        }
        s.println("\t-------------------------------");
        StackTraceElement[] trace = getStackTrace();
        for (StackTraceElement aTrace : trace) {
            s.println("\tat " + aTrace);
        }
        Throwable ourCause = getCause();
        if (ourCause != null) {
            ourCause.printStackTrace(s);
        }
        s.flush();
    }

}
