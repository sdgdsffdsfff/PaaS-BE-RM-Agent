package com.ai.platform.agent.client.incoming.processor.command;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.entity.UserChannelInfo;
import com.ai.platform.agent.client.incoming.processor.AbstractCommandProcessor;
import com.ai.platform.agent.client.util.ShellChannelCollectionUtil;
import com.ai.platform.agent.exception.AgentServerException;

import io.netty.channel.ChannelHandlerContext;

/***
 * 接收服务端的命令处理
 * 
 * @author Administrator
 *
 */
public class ExecCommandMessageProcessor extends AbstractCommandProcessor {

	public static Logger logger = LogManager.getLogger(ExecCommandMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		String sid = super.command.getSid();
		String command = super.command.getMessage();
		logger.info("服务端发来指令，内容：{}", super.command.getMessage());

		String key = sid;

		if (!ShellChannelCollectionUtil.userChannelMap.containsKey(key)) {
			throw new AgentServerException("当前会话容器未找到sid:{}的会话信息");
		}

		UserChannelInfo usci = ShellChannelCollectionUtil.userChannelMap.get(key);

		usci.checkChannelIsOpen();
		
		//下面应该放到一个线程中执行
		try {
			InputStream instream = usci.getInstream();
			OutputStream outstream = usci.getOutstream();
			
			outstream.write(command.getBytes());
			outstream.flush();
			
			Thread.sleep(1000);
			// 获取命令执行的结果
			if (instream.available() > 0) {
				byte[] data = new byte[instream.available()];
				int nLen = instream.read(data);

				if (nLen < 0) {
					throw new AgentServerException("network error.");
				}

				// 转换输出结果并打印出来
				String temp = new String(data, 0, nLen, "UTF-8");
				System.out.println(temp);
			}
			
		} catch (Exception e) {
			logger.error("执行命令发生异常", e);
			throw new AgentServerException("执行命令发生异常");
		}
		

	}
}
