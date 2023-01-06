package com.example.application;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Instant;

@Service
class ChatService {

	record Message(String userName, String message, Instant time) {
	}

	private final Sinks.Many<Message> chatSink = Sinks.many().multicast().directBestEffort();

	;
	private final Flux<Message> chat = chatSink.asFlux().replay(10).autoConnect();

	;

	public Flux<Message> join() {
		return chat;
	}

	public void send(String message, String userName) {
		chatSink.emitNext(new Message(userName, message, Instant.now()),
				(signalType, emitResult) -> emitResult == Sinks.EmitResult.FAIL_NON_SERIALIZED);
	}

}
