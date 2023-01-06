package com.example.application;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
class LoginView extends VerticalLayout {

	LoginView() {
		setSizeFull();

		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		LoginForm login = new LoginForm();
		login.setAction("login");

		add(new H1("Flow Chat"), login);
	}

}