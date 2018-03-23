package io.strimzi.kafka.bridge.http.converter;

import io.strimzi.kafka.bridge.converter.MessageConverter;
import io.strimzi.kafka.bridge.http.HttpMessage;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.kafka.client.producer.KafkaProducerRecord;


public class HttpDefaultMessageConverter implements MessageConverter<String,byte[],HttpMessage>{

	
	/*topic saved in httpMessage.path()
	 * like this: /topic/
	 * 
	 * */
	@Override
	public KafkaProducerRecord<String, byte[]> toKafkaRecord(String kafkaTopic, HttpMessage message) {
		KafkaProducerRecord<String,byte[]> record = KafkaProducerRecord.create(message.getTopic(), message.getHttpKey(), message.getHttpBody(),message.getHttpPartition());
		return record;
	}

	@Override
	public HttpMessage toMessage(String address, KafkaConsumerRecord<String, byte[]> record) {
		
		return new HttpMessage(record.topic(),record.partition(),record.offset(),record.key(),record.value());
	}

}
