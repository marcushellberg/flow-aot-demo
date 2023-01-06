package com.example.application;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import reactor.core.Disposable;

import java.util.ArrayList;

@Route("")
@PermitAll
class ChatView extends VerticalLayout {

	private final Disposable chatSubscription;

	public ChatView(ChatService service, AuthenticationContext authContext) {

		var logout = new Button("Log out", e -> authContext.logout());
		var list = new MessageList();
		var input = new MessageInput();
		var ui = UI.getCurrent();

		setSizeFull();
		add(logout, list, input);
		expand(list);
		input.setWidthFull();
		setAlignSelf(Alignment.END, logout);

		input.addSubmitListener(e -> {
			service.send(e.getValue(), authContext.getPrincipalName().orElse("Anonymous"));
		});

		chatSubscription = service.join().subscribe(message -> {
			var items = new ArrayList<>(list.getItems());
			items.add(new MessageListItem(message.message(), message.time(), message.userName()));
			ui.access(() -> list.setItems(items));
		});

	}


	@Override
	protected void onDetach(DetachEvent detachEvent) {
		super.onDetach(detachEvent);
		if(chatSubscription != null) {
			chatSubscription.dispose();
		}
	}
}
