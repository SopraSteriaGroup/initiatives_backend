package com.soprasteria.initiatives.commons.testing;

import rx.observers.TestSubscriber;

/**
 * Utilities for Rx testing
 *
 * @author jntakpe
 * @see TestSubscriber
 */
public class CustomTestSubscriber<T> extends TestSubscriber<T> {

    public T assertNoErrorsThenWaitAndGetValue() {
        awaitTerminalEvent();
        assertNoErrors();
        assertValueCount(1);
        return getSingleValue();
    }

    private T getSingleValue() {
        return this.getOnNextEvents().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Impossible d'extraire une valeur de l'observable"));
    }
}
