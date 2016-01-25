package com.ai.platform.agent.web.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.platform.agent.server.ServerMain;

public class InitNettyServerListener implements ServletContextListener {
	
	public static Logger logger = LogManager.getLogger(InitNettyServerListener.class);
	
	private ServerMain server = ServerMain.getInstance();

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		server.destroy();
		logger.info("停止netty服务器完成...");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		new Thread(){
			public void run(){
				server.start();
			}
		}.start();
		logger.info("启动netty服务器完成...");
	}

}
