package com.unidev.polyinsights.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unidev.polyinsights.model.Tenant;
import com.unidev.polyinsights.service.TenantDAO;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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

        VerticalLayout popupContent = new VerticalLayout();
        TextField tenantNameTextField = new TextField("Tenant name");
        Button addTenantButton = new Button("Add Tenant");
        popupContent.addComponent(tenantNameTextField);
        popupContent.addComponent(addTenantButton);

        PopupView popup = new PopupView("Add Tenant", popupContent);
        content.addComponent(popup);
        content.addComponent(new Label("<hr />",Label.CONTENT_XHTML));

        addTenantButton.addClickListener((Button.ClickListener) event -> {
            String name = tenantNameTextField.getValue();
            if (StringUtils.isBlank(name)) {
                Notification.show("Warning", "Empty tenant name", Notification.Type.WARNING_MESSAGE);
                return;
            }

            if (tenantDAO.findOne(name) != null) {
                Notification.show("Warning", "Tenant already exists '" + name + "'", Notification.Type.WARNING_MESSAGE);
                return;
            }

            Tenant tenant = new Tenant();
            tenant.setTenant(name);
            tenant = tenantDAO.save(tenant);
            Notification.show("Notification", "Added tenant with name '" + tenant.getTenant() + "'", Notification.Type.TRAY_NOTIFICATION);
        });

        content.addComponent(new Label("<pre>" +
                "\nVotes:\n" +
                "{\n" +
                "  \"tenant\" : \"cats_wallpapers\",\n" +
                "  \"types\" : {\n" +
                "    \"vote\" : {\n" +
                "      \"name\" : \"vote\",\n" +
                "      \"values\" : [ \"1\", \"2\", \"3\", \"4\", \"5\" ],\n" +
                "      \"interval\" : 1000,\n" +
                "      \"sameInsightInterval\" : 1000\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "\nLikes:\n" +
                "{\n" +
                "  \"tenant\" : \"cats_wallpapers\",\n" +
                "  \"types\" : {\n" +
                "    \"like\" : {\n" +
                "      \"name\" : \"like\",\n" +
                "      \"values\" : [ \"1\", \"2\"],\n" +
                "      \"interval\" : 1000,\n" +
                "      \"sameInsightInterval\" : 1000\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "</pre>",Label.CONTENT_XHTML));

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

            final TextArea codeArea = new TextArea("Tenant details");
            codeArea.setWidth("100%");
            codeArea.setHeight("100%");
            codeArea.setRows(15);
            codeArea.setValue(json);

            layout.addComponent(codeArea);

            Button save = new Button("Save");
            layout.addComponent(save);

            save.addClickListener((Button.ClickListener) event -> {
                String json1 = codeArea.getValue();
                try {
                    Tenant updatedTenant = objectMapper.readValue(json1, Tenant.class);
                    updatedTenant = tenantDAO.save(updatedTenant);

                    Notification.show("Notification", "Record was update for tenant:" + updatedTenant.getTenant(), Notification.Type.TRAY_NOTIFICATION);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }




    }
}
