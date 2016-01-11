package com.ai.platform.agent.util;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;



public class CommonTools {
	public static String getHostAddr() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			StringBuilder ipAddr = new StringBuilder();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				Enumeration<InetAddress> addresses = netInterface
						.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = addresses.nextElement();
					if (ip != null && !ip.isLoopbackAddress()
							&& ip instanceof Inet4Address) {
						ipAddr.append(ip.getHostAddress()).append(";");
					}
				}
			}
			if (ipAddr.length() == 0) {
				return "unknown host";
			} else {
				return ipAddr.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "unknown host";
		}
	}
	


	public static byte[] hex2Ascii(byte[] in) {
		byte[] temp1 = new byte[1];
		byte[] temp2 = new byte[1];
		byte[] out = new byte[in.length * 2];
		int i = 0;
		for (int j = 0; i < in.length; ++i) {
			temp1[0] = in[i];
			temp1[0] = (byte) (temp1[0] >>> 4);
			temp1[0] = (byte) (temp1[0] & 0xF);
			temp2[0] = in[i];
			temp2[0] = (byte) (temp2[0] & 0xF);
			if ((temp1[0] >= 0) && (temp1[0] <= 9))
				out[j] = (byte) (temp1[0] + 48);
			else if ((temp1[0] >= 10) && (temp1[0] <= 15)) {
				out[j] = (byte) (temp1[0] + 87);
			}

			if ((temp2[0] >= 0) && (temp2[0] <= 9))
				out[(j + 1)] = (byte) (temp2[0] + 48);
			else if ((temp2[0] >= 10) && (temp2[0] <= 15)) {
				out[(j + 1)] = (byte) (temp2[0] + 87);
			}
			j += 2;
		}
		return out;
	}

	public static byte[] ascii2Hex(byte[] in) {
		byte[] temp1 = new byte[1];
		byte[] temp2 = new byte[1];
		int i = 0;
		byte[] out = new byte[in.length/2];
		for (int j = 0; i < in.length; ++j) {
			temp1[0] = in[i];
			temp2[0] = in[(i + 1)];
			if ((temp1[0] >= 48) && (temp1[0] <= 57)) {
				int tmp53_52 = 0;
				byte[] tmp53_51 = temp1;
				tmp53_51[tmp53_52] = (byte) (tmp53_51[tmp53_52] - 48);
				temp1[0] = (byte) (temp1[0] << 4);

				temp1[0] = (byte) (temp1[0] & 0xF0);
			} else if ((temp1[0] >= 97) && (temp1[0] <= 102)) {
				int tmp101_100 = 0;
				byte[] tmp101_99 = temp1;
				tmp101_99[tmp101_100] = (byte) (tmp101_99[tmp101_100] - 87);
				temp1[0] = (byte) (temp1[0] << 4);
				temp1[0] = (byte) (temp1[0] & 0xF0);
			}

			if ((temp2[0] >= 48) && (temp2[0] <= 57)) {
				int tmp149_148 = 0;
				byte[] tmp149_146 = temp2;
				tmp149_146[tmp149_148] = (byte) (tmp149_146[tmp149_148] - 48);

				temp2[0] = (byte) (temp2[0] & 0xF);
			} else if ((temp2[0] >= 97) && (temp2[0] <= 102)) {
				int tmp192_191 = 0;
				byte[] tmp192_189 = temp2;
				tmp192_189[tmp192_191] = (byte) (tmp192_189[tmp192_191] - 87);

				temp2[0] = (byte) (temp2[0] & 0xF);
			}
			out[j] = (byte) (temp1[0] | temp2[0]);
			i += 2;
		}
		return out;
	}
	
	public static String getProcessId() {  
	    String pName = ManagementFactory.getRuntimeMXBean().getName();  
	    int index = pName.indexOf('@');  
	    if (index == -1) {  
	        return ""; 
	    }  
	    String pid = pName.substring(0, index);  
	    return pid;  
	}  
}
