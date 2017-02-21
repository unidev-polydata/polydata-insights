package com.unidev.polyinsights;

import com.unidev.platform.j2ee.common.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Public exposed service endpoint
 */
@RestController
public class PublicController {

    private static Logger LOG = LoggerFactory.getLogger(PublicController.class);

    @Autowired
    private HttpServletRequest servletRequest;

    @Autowired
    private WebUtils webUtils;

    @GetMapping("/api/public")
    public String handle() {
        LOG.info("Public request {} {}", webUtils.listAllHeaders(servletRequest),  servletRequest);
        return "Public page";
    }

}
