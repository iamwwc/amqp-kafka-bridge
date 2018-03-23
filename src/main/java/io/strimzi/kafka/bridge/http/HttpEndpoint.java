package io.strimzi.kafka.bridge.http;

import io.strimzi.kafka.bridge.Endpoint;

public class HttpEndpoint implements Endpoint<HttpConnection>{
	private HttpConnection connection;
	public HttpEndpoint (HttpConnection connection) {
		this.connection = connection;
	
	}
	
	@Override
	public HttpConnection get() {
		return this.connection;
	}
}
