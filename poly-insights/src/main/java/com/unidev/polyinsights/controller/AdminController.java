package com.unidev.polyinsights.controller;

import com.unidev.polyinsights.service.TenantDAO;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controller for management of insights
 */
@SpringUI
@Theme("valo")
public class AdminController extends UI {


    @Autowired
    private TenantDAO tenantDAO;

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout content = new VerticalLayout();
        setContent(content);

        HorizontalLayout titleBar = new HorizontalLayout();
        content.addComponent(titleBar);


        ComboBox tenants = new ComboBox("Available tenants");
        tenants.setInvalidAllowed(false);
        tenants.setNullSelectionAllowed(false);

        tenantDAO.findAll().forEach(item -> tenants.addItems(item.getTenant()));


        content.addComponent(tenants);

    }
}
