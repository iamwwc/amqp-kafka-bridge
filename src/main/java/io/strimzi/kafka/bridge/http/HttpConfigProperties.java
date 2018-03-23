package io.strimzi.kafka.bridge.http;

public class HttpConfigProperties {
	private final String HTTP_DEFAULT_HOST = "0.0.0.0";
	private final int HTTP_DEFAULT_LISTEN_PORT = 8080;
	private final String HTTP_DEFAULT_MESSAGE_CONVERTER = "HttpDefaultMessageConverter";
	
	private String HttpDefaultMessageConverter  = HTTP_DEFAULT_MESSAGE_CONVERTER;
	private int httpDefaultListenPort = HTTP_DEFAULT_LISTEN_PORT;
	private String httpDefaultHost = HTTP_DEFAULT_HOST;
	
	public String getHost() {
		return this.httpDefaultHost;
	}
	
	public String getMessageConverter() {
		return this.HttpDefaultMessageConverter;
	}
	
	public int getPort() {
		return this.httpDefaultListenPort;
	}
}
