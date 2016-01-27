package com.ai.platform.agent.client.incoming.processor.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.client.incoming.processor.AbstractSimpleCommandProcessor;
import com.ai.platform.agent.entity.SimpleCommandReqInfo;
import com.ai.platform.agent.exception.AgentServerException;
import com.ai.platform.agent.util.AgentServerCommandConstant;
import com.ai.platform.agent.util.ByteArrayUtil;
import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;

/***
 * 接收服务端的命令处理
 * 
 * @author Administrator
 *
 */
public class SimpleCommandMessageProcessor extends AbstractSimpleCommandProcessor {

	public static Logger logger = LogManager.getLogger(SimpleCommandMessageProcessor.class);

	@Override
	public void proc(ChannelHandlerContext ctx) throws AgentServerException {
		String message = super.command.getMessage();
		SimpleCommandReqInfo msgInfo = JSON.parseObject(message, SimpleCommandReqInfo.class);

		logger.info("服务端发来指令，内容：{}", super.command.getMessage());

		// 下面应该放到一个线程中执行
		try {
			 String[] cmds = { "/bin/sh", "-c", msgInfo.getCommand() }; //linux
//			String[] cmds = { "cmd.exe", "/C", msgInfo.getCommand() }; //window
			Process pro = Runtime.getRuntime().exec(cmds);
			pro.waitFor();
			InputStream in = pro.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in, "GBK"));
			String line = null;
			StringBuffer sb = new StringBuffer("");
			while ((line = read.readLine()) != null) {
				sb.append(line + "\n");
			}

			msgInfo.setCode("0");
			msgInfo.setMsg(sb.toString());
			logger.info("执行结果：{}", msgInfo);

		} catch (Exception e) {
			logger.error("执行命令发生异常", e);
			msgInfo.setCode("1");
			msgInfo.setMsg("写入文件失败");
		} finally {
			byte[] contentArray = ByteArrayUtil.mergeByteArray(AgentServerCommandConstant.PACKAGE_TYPE_SIMP_COMMAND,
					JSON.toJSONString(msgInfo).getBytes());
			ctx.channel().writeAndFlush(contentArray);
		}

	}
}
