package com.ai.platform.agent.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.incoming.AgentClientInitializer;
import com.ai.platform.agent.client.outgoing.AuthDataPacket;
import com.ai.platform.agent.client.util.ShellChannelCollectionUtil;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.util.AgentConstant;
import com.ai.platform.agent.util.ConfigInit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientMain {
	static Logger logger = LogManager.getLogger(ClientMain.class);

	public static void main(String[] args) throws AgentServerException {
		String serverAddr = ConfigInit.serverConstant.get(AgentConstant.SERVER_IP);
		int port = Integer.valueOf(ConfigInit.serverConstant.get(AgentConstant.SERVER_PORT));
		while (true) {
			EventLoopGroup group = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_KEEPALIVE, true).handler(new AgentClientInitializer());
			try {
				// 发起异步链接操作
				ChannelFuture channelFuture = bootstrap.connect(serverAddr, port);
				channelFuture.sync();
				// 向服务器发送身份验证信息
				AuthDataPacket adp = new AuthDataPacket();
				byte[] authPacket = adp.genDataPacket(null);
				channelFuture.channel().writeAndFlush(authPacket);
				logger.info("agent客户端[{}]发起上线操作，发送身份验证信息：{}",
						adp.getAuthJson().getString(AgentConstant.CHANNEL_SHOW_KEY), adp.getAuthJson());
				channelFuture.channel().closeFuture().sync();
			} catch (Exception e) {
				logger.error(e);
			} finally {
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("sleep fail,{}", e);
				}
				ShellChannelCollectionUtil.userChannelMap.clear();
				group.shutdownGracefully();
			}
		}
	}
}
