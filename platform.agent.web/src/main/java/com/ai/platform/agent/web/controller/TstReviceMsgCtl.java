package com.ai.platform.agent.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.platform.agent.web.util.TstMsgConstants;

@Controller
public class TstReviceMsgCtl {

	private static Logger logger = LogManager.getLogger(TstReviceMsgCtl.class);

	@RequestMapping(value = "/reviceMsg/{sid}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String reciveMsg(@PathVariable("sid") String sid, @RequestBody String jsonStr) throws Exception {
		
		TstMsgConstants.put(sid, jsonStr);
		return "index";
	}

}
