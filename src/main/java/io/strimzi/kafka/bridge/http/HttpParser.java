package io.strimzi.kafka.bridge.http;

import java.util.HashMap;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public class HttpParser {
	private HttpParser() {
		
	}
	
	private HttpMessage doParse(Buffer buffer) {
		HttpMessage message = new HttpMessage();
		String completedRequest = buffer.toString();
		HashMap<String,String> params = new HashMap<String,String>();
		
		String header = new String(completedRequest.substring(0, completedRequest.indexOf("\r\n\r\n")));
		HashMap<String,String> headerMap = new HashMap<String,String>(); 
    	
		String requestLine = header.substring(0, header.indexOf("\r\n"));
    	String[] line = requestLine.split(" |\\?|&");
		
    	message.setHttpRequestMethod(line[0]);
		message.setHttpPath(line[1]);
		for(int i = 2 ;i < line.length-1 ; ++i) {
			String[] key_or_value = line[i].split("=");
			params.put(key_or_value[0], key_or_value[1]);
		}
		String[] requestHeader = header.substring(header.indexOf("\r\n")+2).split("\r\n");
		for(int i = 0 ; i < requestHeader.length ; ++i) {
    		headerMap.put(requestHeader[i].split(": ")[0], requestHeader[i].split(": ")[1]);
    	}
    	
		message.setHttpRequestHeader(headerMap);
		
		return message;
	}
	
	public static final HttpMessage create(Buffer buffer) {
		HttpParser httpParser = new HttpParser();
		return httpParser.doParse(buffer);
	}
	
	public static  void writeToSocket(NetSocket socket, HttpMessage message) {
		StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n");
		long  bodyLength = message.getBodyLength();
		response.append("Content-Length: " + String.valueOf(bodyLength) + "\r\n")
				.append("Connection: Keep-alive\r\n")
				.append("\r\n");
		socket.write(Buffer.buffer(response.toString()));
	}
}
