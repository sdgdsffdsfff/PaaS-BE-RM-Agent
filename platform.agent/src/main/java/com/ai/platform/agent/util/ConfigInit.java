package com.ai.platform.agent.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigInit {

	public static Logger logger = LogManager.getLogger(ConfigInit.class);

	public static Map<String, String> serverConstant = new HashMap<String, String>();

	static {
		Properties p = new Properties();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					ConfigInit.class.getClassLoader().getResourceAsStream(AgentConstant.AGENT_CONFIG_FILE_NAME),
					"UTF-8"));
			p.load(bf);
			for (Entry<Object, Object> tmpEntry : p.entrySet()) {
				serverConstant.put((String) tmpEntry.getKey(), (String) tmpEntry.getValue());
				logger.info("初始化key[{}]的值为{{}}", (String) tmpEntry.getKey(), (String) tmpEntry.getValue());
			}
		} catch (IOException e) {
			logger.error("{}配置文件读取异常");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(ConfigInit.serverConstant);
	}
}
