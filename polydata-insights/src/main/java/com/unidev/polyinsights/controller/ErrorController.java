package com.unidev.polyinsights.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Error handling controller
 */
@RestController
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
