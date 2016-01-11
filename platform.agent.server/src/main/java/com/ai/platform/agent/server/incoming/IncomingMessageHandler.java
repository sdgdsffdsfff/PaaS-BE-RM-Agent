package com.ai.platform.agent.server.incoming;

import com.ai.platform.agent.server.exception.MesageParserException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class IncomingMessageHandler extends SimpleChannelInboundHandler<byte[]> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		if (msg.length < 3) {
			throw new MesageParserException("msg[] is too short.");
		}
		byte[] data = new byte[msg.length - 2];
		System.arraycopy(msg, 2, data, 0, data.length);
		IMessageProcessor messageProcessor = null;
		
		messageProcessor = new StringMessageProcessor();
		//message format
		/*
		switch (msg[0]) {
		case 0:
			switch (msg[1]) {
			case 0:
				messageProcessor = new StringMessageProcessor();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		*/

		if(messageProcessor == null){
			throw new MesageParserException("unkown message format, message type:" + msg[0] + msg[1]);
		}
		messageProcessor.init(data);
		
		messageProcessor.proc(ctx);
	}
}