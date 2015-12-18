package com.ai.platform.agent.client.incoming;

import io.netty.channel.ChannelHandlerContext;

import com.ai.platform.agent.client.exception.MesageParserException;

public interface IMessageProcessor {
	public void init(byte[] msg) throws MesageParserException;
	
	public void proc(ChannelHandlerContext ctx);
}
