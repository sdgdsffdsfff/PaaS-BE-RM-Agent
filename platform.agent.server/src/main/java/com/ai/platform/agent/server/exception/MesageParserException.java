package com.ai.platform.agent.server.exception;

public class MesageParserException extends AgentServerException {
	private static final long serialVersionUID = 5509483141617143718L;
	
	public MesageParserException(String message){
		super(message);
	}

	public MesageParserException(String message, Exception cause){
		super(message,cause);
	}
}
