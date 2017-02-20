package com.unidev.service;

import com.unidev.platform.j2ee.common.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller for "internal" operations usage
 */
@RestController
public class InternalController {

    private static Logger LOG = LoggerFactory.getLogger(InternalController.class);

    @Autowired
    private HttpServletRequest servletRequest;

    @Autowired
    private WebUtils webUtils;

    @GetMapping("/internal")
    public String handle() {
        LOG.info("Internal  request {}  {}", webUtils.listAllHeaders(servletRequest),  servletRequest);
        return "Internal page";
    }

}
