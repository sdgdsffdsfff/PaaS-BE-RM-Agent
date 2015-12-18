package com.ai.platform.agent.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.incoming.IncomingMessageHandler;


public class ClientMain {
	static Logger logger = LogManager.getLogger(ClientMain.class);
	
	public static void main(String[] args){
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            p.addLast("frameEncoder", new LengthFieldPrepender(4));
                            p.addLast("decoder", new ByteArrayDecoder());
                            p.addLast("encoder", new ByteArrayEncoder());
                            p.addLast(new IncomingMessageHandler());
						}
					});
			// 发起异步链接操作
			ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 3001);
			channelFuture.sync();
			
			channelFuture.channel().writeAndFlush(args[0].getBytes());

			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.error(e);
		} finally {
			group.shutdownGracefully();
		}
	}
}
