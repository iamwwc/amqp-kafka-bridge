package io.strimzi.kafka.bridge.http;

public class HttpErrorConditionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String error;
	public HttpErrorConditionException(String message) {
		super(message);
	}
	
	public HttpErrorConditionException(String error, String message, Throwable cause) {
		super(message,cause);
		this.error = error;
	}
}
