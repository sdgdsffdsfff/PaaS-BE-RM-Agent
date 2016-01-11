package com.ai.platform.agent.entity;

public class AgentSftpInfo {
	
	private HostFileInfo srcFileSftpInfo;
	
	private HostFileInfo descFileSftpInfo;

	public HostFileInfo getSrcFileSftpInfo() {
		return srcFileSftpInfo;
	}

	public void setSrcFileSftpInfo(HostFileInfo srcFileSftpInfo) {
		this.srcFileSftpInfo = srcFileSftpInfo;
	}

	public HostFileInfo getDescFileSftpInfo() {
		return descFileSftpInfo;
	}

	public void setDescFileSftpInfo(HostFileInfo descFileSftpInfo) {
		this.descFileSftpInfo = descFileSftpInfo;
	}
	
}
