package io.strimzi.kafka.bridge.http;



import java.util.HashMap;
import io.vertx.core.buffer.Buffer;

/*for producer, body include messages want to send to kakfa
 * for consumer, body include messages want to send to http client 
 * */

/*HttpParser.java don't set followed fields(group,partition,offset,timeStamp) when parse http header
 * these used when use MessageConverter.toMessage, store info from consumerRecord,
 * so that httpParser.writeToSocket can send response according httpMessage
 * */

public class HttpMessage {
	
	private String path; 
	private String httpRequestMethod;
	private HashMap<String,String> requestParams;
	private String topic;
	
	private String group;
	private int partition;
	private long offset;
	private String timeStamp;
	private String key;
	
	private HashMap<String,String> requestHeader;
	
	private byte[] httpBody;

	public HttpMessage(String topic, int partition,long offset, String key, byte[] body) {
		
	}
	
	public HttpMessage() {
		
	}
	
	public HttpMessage setHttpRequestMethod(String requestMethod) {
		this.httpRequestMethod = requestMethod;
		return this;
	}
	public String getHttpKey() {
		return this.key;
	}
	
	public HttpMessage setHttpKey(String key) {
		this.key = key;
		return this;
	}
	
	public String getHttpRequestMethod() {
		return this.httpRequestMethod;
	}
	
	public HttpMessage setHttpRequestParams(HashMap<String,String> params){
		this.requestParams = params;
		return this;
	}
	
	public HashMap<String,String> getHttpRequestParams(){
		return this.requestParams;
	}
	
	public  HttpMessage setHttpRequestHeader(HashMap<String,String> map) {
		this.requestHeader = map;
		return this;
	}
	
	public HashMap<String,String> getHttpRequestHeader(){
		return this.requestHeader;
	}
	
	public String getTopic() {
		return this.topic;
	}
	
	public HttpMessage setTopic(String topic) {
		this.topic = topic;
		return this;
	}
	
	public long getBodyLength() {
		return this.httpBody.length;
	}
	
	public HttpMessage setHttpBody(Buffer buffer) {
		this.httpBody = buffer.getBytes();
		return this;
	}
	
	public byte[] getHttpBody() {
		return this.httpBody;
	}
	
	public HttpMessage setHttpPath(String path) {
		this.path = path;
		return this;
	}
	
	public String getHttpPath(){
		return this.path;
	}
	
	public int getHttpPartition() {
		return this.partition;
	}
	
	public HttpMessage setHttpPartition(int partition) {
		this.partition = partition;
		return this;
	}
	
	public String getHttpTimestamp() {
		return this.timeStamp;
	}
	
	public HttpMessage setHttpTimestamp(String timeStamp) {
		this.timeStamp = timeStamp;
		return this;
	}
	
	public String getHttpGroup() {
		return this.group;
	}
	
	public HttpMessage setHttpGroup(String group) {
		this.group = group;
		return this;
	}
	
	public long getHttpOffset() {
		return this.offset;
	}
	
	public HttpMessage setHttpOffset(long offset) {
		this.offset = offset;
		return this;
	}
}