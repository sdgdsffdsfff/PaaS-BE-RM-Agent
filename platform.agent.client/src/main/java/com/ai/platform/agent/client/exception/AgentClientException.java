package com.ai.platform.agent.client.exception;

/**
 * Agent Server 全局异常
 * @author wusheng
 *
 */
public class AgentClientException extends Exception {
	private static final long serialVersionUID = 6050485630751883906L;

	public AgentClientException(String message){
		super(message);
	}
	
	public AgentClientException(String message, Exception cause){
		super(message,cause);
	}
}
