package com.unidev.polyinsights.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unidev.polyinsights.model.Tenant;
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

    @Autowired
    private ObjectMapper objectMapper;

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

    protected void showTenantDetails(VerticalLayout layout, String tenantName) {
        layout.removeAllComponents();

        Label label = new Label("Tenant: " + tenantName);
        layout.addComponent(label);

        Tenant tenant = tenantDAO.findOne(tenantName);

        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tenant);

            TextArea codeArea = new TextArea("Tenant instance");
            codeArea.setWidth("100%");
            codeArea.setHeight("100%");
            codeArea.setRows(15);
            codeArea.setValue(json);

            layout.addComponent(codeArea);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }




    }
}
