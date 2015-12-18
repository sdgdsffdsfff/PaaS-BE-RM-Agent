package com.ai.platform.agent.server.incoming;

import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

import com.ai.platform.agent.server.exception.MesageParserException;

public class StringMessageProcessor implements IMessageProcessor {
	private String message;

	@Override
	public void init(byte[] msg) throws MesageParserException {
		try {
			message = new String(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new MesageParserException("parse byte[] to string failure.",
					e);
		}
	}

	@Override
	public void proc(final ChannelHandlerContext ctx) {
		System.out.println(message);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
				ctx.channel().writeAndFlush("dddddddddd".getBytes());
			}
		}).start();
	}
}
