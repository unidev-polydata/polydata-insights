package com.unidev.polyinsights.service;

import com.unidev.platform.common.exception.UnidevRuntimeException;

/**
 * Exception thrown when insight wasn't accepted
 */
public class InsightNotAccepted extends UnidevRuntimeException {

    public InsightNotAccepted(String message) {
        super(message);
    }
}
