package com.ai.platform.agent.util;


import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CiperTools {
	
	private static final Logger log = LogManager.getLogger(CiperTools.class);

	public static final String KEY_ALGORITHM = "DES";
	public static final String KEY_TriDes = "DESede";
	public static final String DES_TriDes_ALGORITHM = "DESede/ECB/NoPadding";
	public static final String DES_ECB_ALGORITHM = "DES/ECB/PKCS5Padding";
	public static final String DES_CBC_ALGORITHM = "DES/CBC/PKCS5Padding";
	public static final String DES_CBC_NOPADDING = "DES/CBC/NoPadding";
	public static final String SECURITY_KEY = "92ba640738a413b0";
	public static final byte[] DES_CBC_IV = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

	public static byte[] generateKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
			keyGenerator.init(56);
			SecretKey secretKey = keyGenerator.generateKey();
			return CommonTools.hex2Ascii(secretKey.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("exception:" + e);
		}
		return null;
	}

	private static Key toKey(byte[] key) {
		try {
			DESKeySpec des = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
			SecretKey secretKey = keyFactory.generateSecret(des);
			return secretKey;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("exception:" + e);
		}
		return null;
	}

	public static byte[] encrypt(byte[] data, byte[] key, String algorithm) {
		try {
			Key k = toKey(key);
			Cipher cipher = Cipher.getInstance(algorithm);
			if(DES_CBC_ALGORITHM.equals(algorithm) || DES_CBC_NOPADDING.equals(algorithm)) {
				IvParameterSpec spec = new IvParameterSpec(DES_CBC_IV);
				cipher.init(Cipher.ENCRYPT_MODE, k, spec);
			}else {
				cipher.init(Cipher.ENCRYPT_MODE, k);
			}
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("exception:" + e);
		}
		return null;
	}

	public static byte[] decrypt(byte[] data, byte[] key, String algorithm) {
		try {
			Key k = toKey(key);
			Cipher cipher = Cipher.getInstance(algorithm);
			if(DES_CBC_ALGORITHM.equals(algorithm) || DES_CBC_NOPADDING.equals(algorithm)) {
				IvParameterSpec spec = new IvParameterSpec(DES_CBC_IV);
				cipher.init(Cipher.DECRYPT_MODE, k,spec);
			}else {
				cipher.init(Cipher.DECRYPT_MODE, k);
			}
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("exception:" + e);
		}
		return null;
	}
	
	public static String encrypt(String data) {
		byte[] aa = encrypt(data.getBytes(), CommonTools.ascii2Hex(SECURITY_KEY.getBytes()),DES_ECB_ALGORITHM);
		return new String(CommonTools.hex2Ascii(aa));
	}
	
	public static String decrypt(String data) {
		byte[] aa = CommonTools.ascii2Hex(data.getBytes());
		return new String(decrypt(aa, CommonTools.ascii2Hex(SECURITY_KEY.getBytes()),DES_ECB_ALGORITHM));
	}
	
	public static String encrypt(String data, String algorithm) {
		byte[] aa = encrypt(data.getBytes(), CommonTools.ascii2Hex(SECURITY_KEY.getBytes()),algorithm);
		return new String(CommonTools.hex2Ascii(aa));
	}
	
	public static String decrypt(String data, String algorithm) {
		byte[] aa = CommonTools.ascii2Hex(data.getBytes());
		return new String(decrypt(aa, CommonTools.ascii2Hex(SECURITY_KEY.getBytes()),algorithm));
	}
	
	public static byte[] paddingZero(byte[] in) {
		if(in == null || in.length == 0) {
			return null;
		}
		int inLen = in.length;
		int m = inLen % 8;
		byte[] out = null;
		if(m == 0) {
			out = new byte[inLen];
		}else {
			out = new byte[inLen + 8 - m];
		}
		int outLen = out.length;
		for(int i=0; i<outLen; i++) {
			if(i<inLen) {
				out[i] = in[i];
			}else {
				out[i] = 0x00;
			}
		}
		return out;
	}
	public static byte[] XOR(byte[] edata, byte[] temp) {
        byte [] result = new byte[8];
        for (int i = 0 , j = result.length ; i < j; i++) {
                result [i] = (byte) (edata[i] ^ temp[i]);
        }
        return result;
   }
	public static byte[] mac_encrypt(byte[] data, byte[] MACKEY) {
		//进行分组
        int group = (data.length + (8 - 1)) / 8;
        //偏移量
        int offset = 0 ;
        //输入计算数据
        byte[] edata = null;
        for(int i = 0 ; i < group; i++){
                byte[] temp = new byte[8];
                if(i != group - 1){ 
                        System.arraycopy(data, offset, temp, 0, 8);
                        offset += 8;
                }else{//只有最后一组数据才进行填充0x00
                        System.arraycopy(data, offset, temp, 0, data.length - offset);
                }
                if(i != 0){//只有第一次不做异或
                        temp = XOR(edata,temp);
                }
                edata = encrypt(temp,MACKEY,DES_CBC_NOPADDING);
        }
		return edata;
	}
	public static byte[] mac_decrypt(byte[] data,byte[] MACKEY) {
		return decrypt(data,MACKEY,DES_CBC_NOPADDING);
	}
	
	public static byte[] trides_encrypt(byte key[], byte data[]) {
		try {
			byte[] k = new byte[24];
			int len = data.length;
			if(data.length % 8 != 0){
				len = data.length - data.length % 8 + 8;
			}
			byte [] needData = null;
			if(len != 0)
				needData = new byte[len];
			for(int i = 0 ; i< len ; i++){
				needData[i] = 0x00;
			}
			System.arraycopy(data, 0, needData, 0, data.length);
			if (key.length == 16) {
				System.arraycopy(key, 0, k, 0, key.length);
				System.arraycopy(key, 0, k, 16, 8);
			} else {
				System.arraycopy(key, 0, k, 0, 24);
			}
			KeySpec ks = new DESedeKeySpec(k);
			SecretKeyFactory kf = SecretKeyFactory.getInstance(KEY_TriDes);
			SecretKey ky = kf.generateSecret(ks);
			Cipher c = Cipher.getInstance(DES_TriDes_ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, ky);
			return c.doFinal(needData);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static byte[] trides_decrypt(byte key[], byte data[]) {
		try {
			byte[] k = new byte[24];

			int len = data.length;
			if(data.length % 8 != 0){
				len = data.length - data.length % 8 + 8;
			}
			byte [] needData = null;
			if(len != 0)
				needData = new byte[len];
			
			for(int i = 0 ; i< len ; i++){
				needData[i] = 0x00;
			}
			
			System.arraycopy(data, 0, needData, 0, data.length);
			
			if (key.length == 16) {
				System.arraycopy(key, 0, k, 0, key.length);
				System.arraycopy(key, 0, k, 16, 8);
			} else {
				System.arraycopy(key, 0, k, 0, 24);
			}
			KeySpec ks = new DESedeKeySpec(k);
			SecretKeyFactory kf = SecretKeyFactory.getInstance(KEY_TriDes);
			SecretKey ky = kf.generateSecret(ks);

			Cipher c = Cipher.getInstance(DES_TriDes_ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, ky);
			return c.doFinal(needData);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static void main(String[] args) {
		//String str = DataConverter.bytesToHexString((CiperTools.decrypt(DataConverter.hexStringToByte(encData), DataConverter.hexStringToByte("44444444444444444444444444444444"), "DES/CBC/NoPadding")));
		//String str = DataConverter.bytesToHexString((CiperTools.trides_decrypt(DataConverter.hexStringToByte("0DCEE80BA1EF6C6536E69C450CC870F3"),DataConverter.hexStringToByte(encData))));
//		byte[] in = orign.getBytes();
//		System.out.println("00:" + new String(CiperTools.generateKey()));
//		byte[] out = paddingZero(in);
//		System.out.println("00:" + new String(CommonTools.hex2Ascii(out)));
		String aa = encrypt("mysql321");
		//byte[] aa = trides_encrypt(DataConverter.hexStringToByte("0DCEE80BA1EF6C6536E69C450CC870F3"),DataConverter.hexStringToByte(encData)));
		//d10bb6123a72de91695c14346c9469e7
		//d10bb6123a72de91a2bb2489b69613b4
		System.out.println("aa:" + new String(aa));
		
		String s=decrypt("310bd7a0cd81c6aaa88587df2a24f820");
		System.out.println("s:"+s);
//		System.out.println("aa:" + new String(CiperTools.generateKey()));
//		System.out.println("bb:" + new String(bb1));
		 //zak(mackey)
       /* byte[] MACKEY = DataConverter.str2cbcd("8F37EADF07CB5232");
        		//strToBcd("8F37EADF07CB5232");
        //待计算数据        
        //byte[] data = DataConverter.stringToBytes("0200702004C000C010532000008600898000005018600000000000000100000047021F9106323330303030303133303130303230303030303036313000000000000000000047525108980001FFFFFFFF00008600898000005018741ECF4A010000010001863C00000064020000000000017132B6B800114000000800000003303030","iso8859-1");
        byte[] data = DataConverter.hexStringToByte("0200702004C000C010532000008600898000005018600000000000000100000047021F9106323330303030303133303130303230303030303036313000000000000000000047525108980001FFFFFFFF00008600898000005018741ECF4A010000010001863C00000064020000000000017132B6B800114000000800000003303030");
        
        //进行分组
        int group = (data.length + (8 - 1)) / 8;
        //偏移量
        int offset = 0 ;
        //输入计算数据
        byte[] edata = null;
        for(int i = 0 ; i < group; i++){
                byte[] temp = new byte[8];
                if(i != group - 1){ 
                        System.arraycopy(data, offset, temp, 0, 8);
                        offset += 8;
                }else{//只有最后一组数据才进行填充0x00
                        System.arraycopy(data, offset, temp, 0, data.length - offset);
                }

                if(i != 0){//只有第一次不做异或
                        temp = XOR(edata,temp);
                }
                edata = desedeEn(MACKEY,temp);
        }*/
        //System.err.println(ConvertUtil.bcdToStr(edata));
	}

}
