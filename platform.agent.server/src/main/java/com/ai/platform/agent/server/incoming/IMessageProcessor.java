package com.ai.platform.agent.server.incoming;

import io.netty.channel.ChannelHandlerContext;

import com.ai.platform.agent.server.exception.MesageParserException;

public interface IMessageProcessor {
	public void init(byte[] msg) throws MesageParserException;
	
	public void proc(ChannelHandlerContext ctx);
}
