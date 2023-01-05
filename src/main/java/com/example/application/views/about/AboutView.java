package com.example.application.views.about;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("About")
@PermitAll
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

	TextField name = new TextField("Name");

	EmailField email = new EmailField("Email");

	public AboutView() {
		var binder = new BeanValidationBinder<>(Person.class);
		binder.bindInstanceFields(this);
		binder.readBean(new Person());

		var saveButton = new Button("Save", e -> {
			var updated = new Person();
			if (binder.writeBeanIfValid(updated)) {
				Notification.show("Saved " + updated.getName());
			}
		});

		var grid = new Grid<>(Person.class);
		grid.setItems(List.of(new Person("Foo", "foo@bar.com"), new Person("Bar", "bar@bar.com")));

		add(name, email, saveButton, grid);
	}

}
