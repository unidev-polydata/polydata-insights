package com.unidev.polyinsights.controller;

import com.unidev.polyinsights.service.TenantDAO;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
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
        content.addComponent(new Label("<hr />",Label.CONTENT_XHTML));
        final VerticalLayout tenantInfo = new VerticalLayout();
        content.addComponent(tenantInfo);

        tenants.addValueChangeListener((Property.ValueChangeListener) event -> {
            String tenant = event.getProperty().getValue() + "";
            showTenantDetails(tenantInfo, tenant);
        });
    }

    protected void showTenantDetails(VerticalLayout layout, String tenant) {
        layout.removeAllComponents();

        Label label = new Label("Tenant: " + tenant);
        layout.addComponent(label);

    }
}
