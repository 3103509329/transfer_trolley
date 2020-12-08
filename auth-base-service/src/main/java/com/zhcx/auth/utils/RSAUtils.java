package com.zhcx.auth.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {
	
	public static final String KEY_ALGORITHM = "RSA";    
	public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";
	public static final int KEY_SIZE = 1024;
    private static String modulus="009f7a348aaf10439fcae0bd2428f00227247175ed2878b6d3fa189616599d00b315105e6714a32ab7349d86b69c0ae538b294918443490c4b5667c767f65fac135d2855a0684b07d93cc33f1d4c402b171a77a00c0a5d18658f2ccba256360bc85eef7f9fbff6f86b41f84e15c71359e132bb7cb246905c2eba5b6adf6ca36571";
    private static String publicExponent="010001";

	 /**
	  * 生成密钥对。注意这里是生成密钥对KeyMap，再由密钥对获取公私钥  方法每运行一次获得一对不同的公钥私钥。
	  * 
	 * @return
	 */
	public static Map<String , byte [] > getKeyMap(){
		  Map<String, byte[]> keyMap = new HashMap<String, byte[]>(2);
		try{
	       //返回生成指定算法的 public/private 密钥对的 KeyPairGenerator 对象。 参数RSA算法
	       KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(KEY_ALGORITHM);
	       //初始化RSA长度，RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024
	       keyPairGenerator.initialize(KEY_SIZE);
	            //生成一个密钥对
	            KeyPair keyPair = keyPairGenerator.generateKeyPair();
	            //获得公钥
	            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
	            //获得私钥
	            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

	            //可以通过getEncoded()方法获取返回类型为byte[]的数组
	            keyMap.put(PUBLIC_KEY, publicKey.getEncoded());

	            keyMap.put(PRIVATE_KEY, privateKey.getEncoded());

	        }catch(NoSuchAlgorithmException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	           return null;
	        }

	         return keyMap;  
	}

	    /**
	     * 公钥，X509EncodedKeySpec 用于构建公钥的规范
	     * 
	     * @param keyBytes
	     * @return
	     */
	    public static PublicKey restorePublicKey(byte[] keyBytes) {
	        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

	        try {
	            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
	            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
	            return publicKey;
	        } catch (NoSuchAlgorithmException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (InvalidKeySpecException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return null;
	    }

	    /**
	     * 私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
	     * 
	     * @param keyBytes
	     * @return
	     */
	    public static PrivateKey restorePrivateKey(byte[] keyBytes) {

	        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
	                keyBytes);

	            KeyFactory factory;
	            try {
	                factory = KeyFactory.getInstance(KEY_ALGORITHM);
	                  PrivateKey privateKey = factory
	                  .generatePrivate(pkcs8EncodedKeySpec);
	                  return privateKey;
	            } catch (NoSuchAlgorithmException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (InvalidKeySpecException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }


	        return null;
	    }

	   /**
	     * 加密
	     * 
	     * @param key
	     * @param plainText
	     * @return
	     */
	    public static byte[] RSAEncode(PrivateKey key, byte[] plainText) {

	        try {
	            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
	            cipher.init(Cipher.ENCRYPT_MODE, key);
	            return cipher.doFinal(plainText);
	        } catch (NoSuchAlgorithmException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (NoSuchPaddingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (InvalidKeyException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IllegalBlockSizeException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (BadPaddingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return null;

	    }

	    /**
	     *解密
	     * 
	     * @param key
	     * @param encodedText
	     * @return
	     */
	    public static String RSADecode(PublicKey key, byte[] encodedText) {

	        try {
	            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
	            cipher.init(Cipher.DECRYPT_MODE, key);
	            return new String(cipher.doFinal(encodedText));
	        } catch (NoSuchAlgorithmException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (NoSuchPaddingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (InvalidKeyException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IllegalBlockSizeException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (BadPaddingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return null;

	    }
	    
	    
	    
	    
	    
	    /** 此方法用于js加密后，发送到java后端进行解密。
	     * 数据RSA解密 
	     * @param raw 密文 
	     * @return 明文 
	     * @throws Exception 
	     */  
	    public static String decryptreverse(byte [] raw) throws Exception {  
	    	try {  
	            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM,  
	                    new org.bouncycastle.jce.provider.BouncyCastleProvider());  
	            cipher.init(cipher.DECRYPT_MODE, generateRSAPrivateKey());  
	            int blockSize = cipher.getBlockSize();  
	            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);  
	            int j = 0;  
	  
	            while (raw.length - j * blockSize > 0) {  
	                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));  
	                j++;  
	            } 
	            StringBuffer s=new StringBuffer(new String(bout.toByteArray()));
	            return s.reverse().toString();  
	        } catch (Exception e) {  
	            throw new Exception(e.getMessage());  
	        }  
	    }
	    
	    /** 
	     * * 生成私钥(固定) * 
	     *  
	     * @return RSAPrivateKey *
	     * @throws Exception 
	     */  
	    public static RSAPrivateKey generateRSAPrivateKey() {  
	    	KeyFactory keyFac = null;  
	    	try {  
	    		keyFac = KeyFactory.getInstance(KEY_ALGORITHM,  
	    				new org.bouncycastle.jce.provider.BouncyCastleProvider());  
	    	} catch (Exception ex) {  
	    		
	    	}  
	    	BigInteger modulu=new BigInteger(CommonUtils.hexStr2ByteArray(modulus));
			BigInteger publicExponen=new BigInteger(CommonUtils.hexStr2ByteArray(publicExponent));
			BigInteger privateExponent=new BigInteger(CommonUtils.hexStr2ByteArray("4e47a50cc1fbc0c5e9c6c0f0c3eb7393150f606c45e6630fcffe4b85b2fd1effc8821d6489bbc1d6e5cbc0f957c4fd5904476ada3a50dbe90b03abdcbb11a11ba17370334b79908a13d1dfed3ba462d42410fadee369eb00ee746fa79f87087ebd93470adfb96cdd992213e8db0e59e73198c5720270720e7636495091b9c045"));
			BigInteger primeP=new BigInteger(CommonUtils.hexStr2ByteArray("00f9087bf05406595d2827596b58cfe3eab1c2d45edd8a23a59398828d649fd2b9ec0f09e5c59af907117089c180e5f82044b406ea817e11db5b26cf54c1479ae7"));
			BigInteger primeQ=new BigInteger(CommonUtils.hexStr2ByteArray("00a3f058120b5d4ba43dd4e6f7b0a887783dc9b5ac78f7abba91524c0ec03dfe1f1aadaedf1886916d8c53fffd9aa84f1886f530308b9e8592d194588348b789e7"));
			BigInteger primeExponentP=new BigInteger(CommonUtils.hexStr2ByteArray("52c5de24105997e45dcf4e6a5eb7a2a2c478e48baf8915b6182bcde0df3c3c176dfdc5a5689bccffeb6d036c9cc25349ae6c440c3804463d29bbe997db3248ab"));
			BigInteger primeExponentQ=new BigInteger(CommonUtils.hexStr2ByteArray("367a1daa1160b54c24c7e4e09631caa2a855a92b611f5c4fa90ee1fb5ce2bcd97532161448c01eff7a4c479aec93d584df0407397a8bf8f32af5d2dbfd84102d"));
			BigInteger crtCoefficient=new BigInteger(CommonUtils.hexStr2ByteArray("6f5b3b1cca6453d6bc35d90a3debab13f86647615e04ad0d7c477e299b7d4ffcc68689b49d013cb2b891da83efe03edbc34474755169747b9df9ec557a055720"));
	    	RSAPrivateCrtKeySpec priKeySpec = new RSAPrivateCrtKeySpec(modulu,publicExponen,privateExponent
	    			,primeP,primeQ,primeExponentP,primeExponentQ,crtCoefficient); 
	    	try {  
	    		return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);  
	    	} catch (InvalidKeySpecException ex) {  
	    		return null;  
	    	} 
	    	
	    }  
	    
	/**
	将String转换为字节数组
	**/
	public static byte[] decryptBASE64(String key) throws Exception{
	        return (new BASE64Decoder()).decodeBuffer(key);
	    }
	/**
	将字节数组转换为String
	**/
	    public static String encryptBASE64(byte[] key) throws Exception{

	        return (new BASE64Encoder()).encodeBuffer(key);
	    } 
	// 将字节转换为十六进制字符串
	    private static String byteToHexString(byte ib) {
	        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
	                'B', 'C', 'D', 'E', 'F' };
	        char[] ob = new char[2];
	        ob[0] = Digit[(ib >>> 4) & 0X0F];
	        ob[1] = Digit[ib & 0X0F];
	        String s = new String(ob);
	        return s;
	    }

	    // 将字节数组转换为十六进制字符串
	    private static String byteArrayToHexString(byte[] bytearray) {
	        String strDigest = "";
	        for (int i = 0; i < bytearray.length; i++)
	        {
	            strDigest += byteToHexString(bytearray[i]);
	        }
	        return strDigest;
	    }

	//16进制字符串转为字节数组
	    public static byte[] hexStringToByte(String hex){
	        int len = (hex.length()/2);
	        byte[] result = new byte[len];
	        char[] achar=hex.toCharArray();
	        for(int i=0;i<len;i++){
	            int pos=i*2;
	            result[i]=(byte)(toByte(achar[pos])<<4|toByte(achar[pos+1]));
	        }

	        return result;
	    }

	    public static int toByte(char c){
	        byte b=(byte)"0123456789ABCDEF".indexOf(c);
	        return b;
	    }
	    
	    public static void main(String[] args) {
	    	String txt="f386590778c213e0750ad275ff699794";//原文
	        //获得密钥对Map
	        Map<String, byte[]> keyMap=getKeyMap();
	        //获得公钥
	        String publicKeyStr;
			try {
			publicKeyStr = encryptBASE64(keyMap.get(PUBLIC_KEY));
			
//			System.out.println(publicKeyStr);
	        //获得密钥
	        String privateKeyStr=encryptBASE64(keyMap.get(PRIVATE_KEY));
//	        System.out.println(privateKeyStr);
	        //将私钥规范
	        PrivateKey privateKey = restorePrivateKey(decryptBASE64(privateKeyStr));
	        //将原文更具私钥加密
	        byte[] encodedText = RSAEncode(privateKey, txt.getBytes("UTF-8"));

	        //私钥签名后的数据 
	        String privateResult = byteArrayToHexString(encodedText);//报文头前面256位的私钥签名后的结果privateResult
//	        System.out.println("签名后的256位数据 " + privateResult);

	        PublicKey publicKey = restorePublicKey(decryptBASE64(publicKeyStr));
	        // 公钥解密
//	        System.out.println("公钥解密: "+ RSADecode(publicKey, hexStringToByte(privateResult)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
			
	}

}
