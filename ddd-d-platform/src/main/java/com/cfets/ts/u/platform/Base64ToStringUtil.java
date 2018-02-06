package com.cfets.ts.u.platform;

public class Base64ToStringUtil {
	private final static String CODE_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";  
    private final static int ORGINAL_LEN = 8;  
    private final static int NEW_LEN = 6;  
	
	public static String decodeBase64(String encodeStr) throws Exception{ 
        StringBuilder sb = new StringBuilder("");  
        for (int i = 0; i < encodeStr.length(); i++){  
            char c = encodeStr.charAt(i);       //把"1tC5sg=="字符串一个个分拆  
            int k = CODE_STR.indexOf(c);        //分拆后的字符在CODE_STR中的位置,从0开始,如果是'=',返回-1  
            if(k != -1){                        //如果该字符不是'='  
                String tmpStr = Integer.toBinaryString(k);  
                int n = 0;  
                while(tmpStr.length() + n < NEW_LEN){  
                    n ++;  
                    sb.append("0");  
                }  
                sb.append(tmpStr);  
            }  
        }  
          
        /** 
         * 8个字节分拆一次，得到总的字符数 
         * 余数是加密的时候补的，舍去 
         */  
        int newByteLen = sb.length() / ORGINAL_LEN;           
          
        /** 
         * 二进制转成字节数组 
         */  
        byte[] b = new byte[newByteLen];  
        for(int j = 0; j < newByteLen; j++){  
            b[j] = (byte)Integer.parseInt(sb.substring(j * ORGINAL_LEN, (j+1) * ORGINAL_LEN),2);  
        }  
          
        /** 
         * 字节数组还原成String 
         */  
        return new String(b, "UTF-8");  
    }  

}
