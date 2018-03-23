package io.strimzi.kafka.bridge.http;


import io.strimzi.kafka.bridge.Endpoint;
import io.strimzi.kafka.bridge.SinkBridgeEndpoint;
import io.strimzi.kafka.bridge.converter.MessageConverter;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;

public class HttpSinkBridgeEndpoint<K,V> extends SinkBridgeEndpoint<K,V>{
	private static final String GROUP_ID_MATCH = "/group.id/";
	private MessageConverter<K,V,HttpMessage> converter;
	private HttpConnection connection;
	private NetSocket socket;
	
	public HttpSinkBridgeEndpoint(Vertx vertx, HttpBridgeConfigProperties bridgeConfigProperties) {
		super(vertx, bridgeConfigProperties);
		
	}

	@Override
	public void open() {
		
		
	}

	@Override
	public void handle(Endpoint<?> endpoint) {
		this.connection = (HttpConnection) endpoint.get();
		this.socket = connection.getSocket();
		HttpConfigProperties httpConfigProperties = (HttpConfigProperties)this.bridgeConfigProperties.getEndpointConfigProperties();
		if(converter == null) {
			try {
				this.converter = (MessageConverter<K,V,HttpMessage>)HttpBridge.instantiateConverter(httpConfigProperties.getMessageConverter());
			} catch (HttpErrorConditionException e) {
				e.printStackTrace();
			}
		}
		
		
		socket.handler(this::processSocketHandler)
		.closeHandler(this::processSocketCloseHandler)
		.drainHandler(this::processSocketDrainHandler);
		
	}
	
	public HttpConnection getHttpConnection() {
		return this.connection;
	}
	
	public void processSocketHandler(Buffer buffer) {
		HttpMessage message = HttpParser.create(buffer);		
		String requestUrl = message.getHttpPath();
		
		int groupIdIndex = requestUrl.indexOf(GROUP_ID_MATCH);
		if(groupIdIndex == -1 
				|| groupIdIndex ==0 
				|| groupIdIndex == requestUrl.length() - HttpSinkBridgeEndpoint.GROUP_ID_MATCH.length()) {
			/*cannot found groupId, connection should be colsed*/
		}else {
			
			/*when client send to bridge through socket ,handler will be called*/

			this.kafkaTopic = requestUrl.substring(0, groupIdIndex).replace('/', '.');
			this.groupId = requestUrl.substring(groupIdIndex + HttpSinkBridgeEndpoint.GROUP_ID_MATCH.length());
			this.initConsumer();
			

			this.setReceivedHandler(this::sendHttpMessage);
			
			this.subscribe();
		}
	}
	
	
	public void processSocketCloseHandler(Void result) {
		log.info("HttpConnection: " + this.connection.toString() + " socket has been closed");
	}
	
	public void processSocketDrainHandler(Void result) {
		
	}
	
	
	/*This function register to setReceivedHandler, will be called when msg come*/
	private void sendHttpMessage(KafkaConsumerRecord<K,V> record) {
		HttpMessage message = this.converter.toMessage(null, record);
		HttpParser.writeToSocket(this.socket, message);
	}



}
