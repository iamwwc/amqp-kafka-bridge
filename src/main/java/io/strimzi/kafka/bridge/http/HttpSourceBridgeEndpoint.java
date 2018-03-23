package io.strimzi.kafka.bridge.http;



import io.strimzi.kafka.bridge.Endpoint;
import io.strimzi.kafka.bridge.SourceBridgeEndpoint;
import io.strimzi.kafka.bridge.converter.MessageConverter;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.kafka.client.producer.KafkaProducerRecord;


public class HttpSourceBridgeEndpoint extends SourceBridgeEndpoint{

	private MessageConverter<String,byte[],HttpMessage> converter;
	private HttpConnection connection;
	private NetSocket socket;
	
	public HttpSourceBridgeEndpoint(Vertx vertx, HttpBridgeConfigProperties bridgeConfigProperties) {
		super(vertx, bridgeConfigProperties);
		
	}

	@Override
	public void handle(Endpoint<?> endpoint) {
		this.connection = (HttpConnection)endpoint.get();
		this.socket = connection.getSocket();
		HttpConfigProperties httpConfigProperties = (HttpConfigProperties) this.bridgeConfigProperties.getEndpointConfigProperties();
		
		socket.handler(this::processSocketHandler)
			.closeHandler(this::processSocketCloseHandler)
			.drainHandler(this::processSocketDrainHandler)
			.endHandler(this::processSocketEndHandler)
			.exceptionHandler(this::processSocketExceptionHandler);
		

		try {
			this.converter = (MessageConverter<String,byte[],HttpMessage>)HttpBridge.instantiateConverter(httpConfigProperties.getMessageConverter());
		} catch (HttpErrorConditionException e) {
			
		}
		
		
	}
	
	/*received message from client through socket, converter to KafkaRecord, then send to kafka*/
	public void processSocketHandler(Buffer buffer) {
		HttpMessage message = HttpParser.create(buffer);
		
		KafkaProducerRecord<String,byte[]> record = converter.toKafkaRecord(message.getTopic(),message);
		KafkaProducerRecord<String,byte[]> kRecord = KafkaProducerRecord.create(
				record.topic(),record.key(),record.value(),record.timestamp(),record.partition()
				);	
		
		this.send(kRecord, (sendResult)->{
			
		});
	}
	
	public void processSocketCloseHandler(Void result) {
		HttpBridge.removeEstablishConnection(this.connection);
	}
	public void processSocketExceptionHandler(Throwable cause) {
		
	}
	
	public void processSocketEndHandler(Void result) {
		
	}
	
	public void processSocketDrainHandler(Void result) {
		
	}
}
