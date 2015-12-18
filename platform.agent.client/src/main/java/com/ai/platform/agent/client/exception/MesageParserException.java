package com.ai.platform.agent.client.exception;

public class MesageParserException extends AgentClientException {
	private static final long serialVersionUID = 5509483141617143718L;
	
	public MesageParserException(String message){
		super(message);
	}

	public MesageParserException(String message, Exception cause){
		super(message,cause);
	}
}
