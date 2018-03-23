package io.strimzi.kafka.bridge.http;

public enum HttpMode {
	SERVER("Server"),CLIENT("Client");
	private final String mode;
	private HttpMode(String mode) {
		this.mode = mode;
	}
}
