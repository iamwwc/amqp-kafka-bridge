package io.strimzi.kafka.bridge.http;

import io.strimzi.kafka.bridge.config.BridgeConfigProperties;

public class HttpBridgeConfigProperties extends BridgeConfigProperties<HttpConfigProperties>{
	public HttpBridgeConfigProperties() {
		super();
		this.endpointConfigProperties = new HttpConfigProperties();
	}
}
