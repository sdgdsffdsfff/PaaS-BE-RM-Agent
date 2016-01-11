package com.ai.platform.agent.client.incoming;

import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

import com.ai.platform.agent.client.exception.MesageParserException;

public class StringMessageProcessor implements IMessageProcessor {
	private String message;

	@Override
	public void init(byte[] msg) throws MesageParserException{
		try {
			message = new String(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new MesageParserException("parse byte[] to string failure." ,e);
		}
	}

	@Override
	public void proc(ChannelHandlerContext ctx) {
		System.out.println(message);
	}

}