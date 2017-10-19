package com.unidev.polydata.insights.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unidev.polydata.insights.model.Tenant;
import com.unidev.polydata.insights.service.TenantDAO;
import com.vaadin.annotations.Theme;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import java.util.List;
import java.util.stream.Collectors;
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

    private static final String EXAMPLE_TENANT = "<pre>" +
        "resultsUri: mongodb://devdb/insights-stats.data\n" +
        "createResultIfMissing: false" +
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
        "      \"values\" : [ \"-1\", \"1\"],\n" +
        "      \"interval\" : 1000,\n" +
        "      \"sameInsightInterval\" : 1000\n" +
        "    }\n" +
        "  }\n" +
        "}\n" +
        "</pre>";

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout content = new VerticalLayout();
        setContent(content);

        HorizontalLayout titleBar = new HorizontalLayout();
        content.addComponent(titleBar);

        ComboBox tenants = new ComboBox("Available tenants");
        List<String> tenantNames = tenantDAO.findAll().stream().map(tenant -> tenant.getTenant() + "")
            .collect(
                Collectors.toList());
        tenants.setDataProvider(new ListDataProvider(tenantNames));
        content.addComponent(tenants);

        VerticalLayout popupContent = new VerticalLayout();
        TextField tenantNameTextField = new TextField("Tenant name");
        Button addTenantButton = new Button("Add Tenant");
        popupContent.addComponent(tenantNameTextField);
        popupContent.addComponent(addTenantButton);

        PopupView popup = new PopupView("Add Tenant", popupContent);
        content.addComponent(popup);
        content.addComponent(new Label("<hr />", ContentMode.HTML));

        addTenantButton.addClickListener((Button.ClickListener) event -> {
            String name = tenantNameTextField.getValue();
            if (StringUtils.isBlank(name)) {
                Notification
                    .show("Warning", "Empty tenant name", Notification.Type.WARNING_MESSAGE);
                return;
            }

            if (tenantDAO.findOne(name) != null) {
                Notification.show("Warning", "Tenant already exists '" + name + "'",
                    Notification.Type.WARNING_MESSAGE);
                return;
            }

            Tenant tenant = new Tenant();
            tenant.setTenant(name);
            tenant = tenantDAO.save(tenant);
            Notification.show("Notification", "Added tenant with name '" + tenant.getTenant() + "'",
                Notification.Type.TRAY_NOTIFICATION);
        });

        final VerticalLayout tenantInfo = new VerticalLayout();
        content.addComponent(tenantInfo);

        tenants.addValueChangeListener((ValueChangeListener) event -> {
            String tenant = event.getValue() + "";
            showTenantDetails(tenantInfo, tenant);
        });

        content.addComponent(new Label(EXAMPLE_TENANT, ContentMode.HTML));
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

                    Notification.show("Notification",
                        "Record was update for tenant:" + updatedTenant.getTenant(),
                        Notification.Type.TRAY_NOTIFICATION);

                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
