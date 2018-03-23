package io.strimzi.kafka.bridge.http;

import io.vertx.core.net.NetSocket;

public class HttpConnection {
	
	private HttpMessage message;
	private NetSocket socket;
	
	public HttpConnection(HttpMessage message, NetSocket socket) {
		this.message = message;
		this.socket = socket;
	}
	
	public HttpConnection(NetSocket socket) {
		this.socket = socket;
	}
	
	public NetSocket getSocket() {
		return this.socket;
	}
	
	
	
	public HttpMessage getHttpMessage() {  
		return this.message;
	}
	
	public void setHttpMessage(HttpMessage message) {
		this.message = message;
	}
}
