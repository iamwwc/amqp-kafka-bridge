package io.strimzi.kafka.bridge.http;



import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.strimzi.kafka.bridge.SinkBridgeEndpoint;
import io.strimzi.kafka.bridge.SourceBridgeEndpoint;
import io.strimzi.kafka.bridge.converter.MessageConverter;
import io.strimzi.kafka.bridge.http.converter.HttpDefaultMessageConverter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

/*	if GET, we think that's producer, write message to kafka through bridge.
 * 	else if POST, we think that's consumer, read message from...
 * 
 * */

/*we assume body has follow format(show line symbol):
 * topic: mytopic1\r\n
 * topic: mytopic2\r\n
 * topic: mytopic3\r\n
 * */

public class HttpBridge extends AbstractVerticle{
	private static final Logger log = LoggerFactory.getLogger(HttpBridge.class); 
	static final int HTTP_DEFAULT_LISTEN_PORT = 8080;
	static final int httpListenPort = HTTP_DEFAULT_LISTEN_PORT;
	private static List<HttpConnection> connectionList ;
	private HttpBridgeConfigProperties bridgeConfigProperties;
	
	private NetServer netServer = null;
	@Override
	public void start(Future<Void> startFuture) {
		NetServerOptions option = this.createNetServerOptions();
		netServer = vertx.createNetServer(option);
		connectionList = new ArrayList<HttpConnection>();
		bridgeConfigProperties = new HttpBridgeConfigProperties();
		
		netServer.connectHandler(netSocket->{
			processRequests(new HttpConnection(netSocket));

		}).listen(listenHandler->{
			if(listenHandler.succeeded()) {
				log.debug("Http bridge started and listening on port {}"+ listenHandler.result().actualPort());
				log.debug("kafka bootstrap servers {}",
						this.bridgeConfigProperties.getKafkaConfigProperties().getBootstrapServers());
				startFuture.complete();
			}else {	
				log.error("http bridge start failed");
				startFuture.fail(listenHandler.cause());
			}
		});
	}
	
	private NetServerOptions createNetServerOptions() {
		NetServerOptions options = new NetServerOptions();
		options.setPort(this.bridgeConfigProperties.getEndpointConfigProperties().getPort());
		options.setHost(this.bridgeConfigProperties.getEndpointConfigProperties().getHost());
		return options;
	}
	
	public void processRequests(HttpConnection connection) {
		if("GET" == connection.getHttpMessage().getHttpRequestMethod()) {
			processHttpGETRequest(connection);
		}else if ("POST" == connection.getHttpMessage().getHttpRequestMethod()) {
			processHttpPOSTRequest(connection);
		}
	}
	
	
	/*
	 * consumer
	 * SinkBridgeEndpoint.java
	 * */
	public void processHttpGETRequest(HttpConnection connection) {
		SinkBridgeEndpoint<?,?> sink = new HttpSinkBridgeEndpoint<>(this.vertx,this.bridgeConfigProperties);
		sink.handle(new HttpEndpoint(connection));
	}
	
	/*producer
	 * SourceBridgeEndpoint.java
	 * */
	public void processHttpPOSTRequest(HttpConnection connection) {		
		SourceBridgeEndpoint source = new HttpSourceBridgeEndpoint(this.vertx,this.bridgeConfigProperties);
		source.handle(new HttpEndpoint(connection));
	}
	
	

	
	static  MessageConverter<?,?,?> instantiateConverter(String className) throws HttpErrorConditionException{
		if(className == null || className.isEmpty()) {
			return (MessageConverter<?,?,?>)new HttpDefaultMessageConverter();
		}else {
			Object instance = null;
			try {
				instance = Class.forName(className).newInstance();
				
			}catch(ClassNotFoundException  
					| IllegalAccessException 
					| InstantiationException e ) {
				//throws new HttpErrorConditionException()
			}
			if(instance instanceof MessageConverter<?,?,?>){
				return (MessageConverter<?,?,?>)instance;
			}else {
				throw new HttpErrorConditionException("cannot instantiate messageconverter");
			}
			
		}
	}
	
	static void removeEstablishConnection(HttpConnection connection) {
		connectionList.remove(connection);		
	}
}
