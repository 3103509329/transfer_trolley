/**  
* @Title:  CommonUtils.java
* @Package com.dayhr.web.util
* @Description: TODO
* @author seff
* @date  2014-10-22 下午2:26:46
* @version 
*/
package com.zhcx.authorization.utils;

import com.zhcx.auth.pojo.AuthUserResp;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
* 通用工具类：字符串判断
* 
* Copyright dayhr2013-2014 All rights reserved
* @ClassName: CommonUtils
* @Description: TODO
* @date 2014-10-22 下午2:26:46
*/
public class CommonUtils {
	
	/*
	 * 车牌号码 7或者8位
	 */
	public final static String carNumColorSOEExpress = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Z]{1}[A-Z0-9]{5,6}";

	public static final String idCardExpress = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";

	/**
	 * 判断男女
	 */
	public final static String sex = "[FM]$";
	
	/**
	 * 从业资格证等级
	 */
	public final static String cerLeve = "[0-4]$";
	/**
	 * 判断String不为空
	 * @param str
	 * @return
	 */
	public static boolean isStrNotNull(String str) {
		return str != null && str.trim().length() != 0&&!"null".equals(str.toString().trim());
	}

	/**
	 * 判断String为空
	 * @param str
	 * @return
	 */
	public static boolean isStrNull(String str) {
		return str == null || str.trim().length() == 0||"null".equals(str.toString().trim());
	}

	/**
	 * 判断List不为空
	 * @param
	 * @return
	 */
	public static boolean isListNotNull(List l) {
		return l != null && l.size() > 0;
	}
	/**
	 * 返回用户的IP地址
	 * @param request
	 * @return
	 */
	public static String toIpAddr(HttpServletRequest request) {
		if(request==null) return "";
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	/**
	 * 判断字符串的第一个字符是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isFirstNumberic(String str) {
		String sub = str.substring(0, 1);
		if (isNumeric(sub)) {
			return true;
		}
		return false;
	}
	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	/**
	 * @Title: checkEmail
	 * @Description: 验证邮箱地址是否正确
	 * @param email
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[_-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * @Title: isMobileNO
	 * @Description: 验证手机号码
	 * @param mobiles
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
//			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Pattern p = Pattern.compile("^1\\d{10}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/***
	 * byte[]数组转成16进制的字符串 
	 * @Title:        bytesToHexString 
	 * @Description:  TODO 
	 * @param:        @param src
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        luojiayong<jiayong_luo@dayhr.com>
	 * @Date          2014-11-12 下午6:58:16
	 */
    public static String bytesToHexString(byte[] src){  
  	    StringBuilder stringBuilder = new StringBuilder("");  
  	    if (src == null || src.length <= 0) {  
  	        return null;  
  	    }  
  	    for (int i = 0; i < src.length; i++) {  
  	        int v = src[i] & 0xFF;  
  	        String hv = Integer.toHexString(v);  
  	        if (hv.length() < 2) {  
  	            stringBuilder.append(0);  
  	        }  
  	        stringBuilder.append(hv);  
  	    }  
  	    return stringBuilder.toString();  
  	} 

    /** 
     * 16进制的字符串表示转成字节数组 
     * 
     * @param hexString 
     *          16进制格式的字符串 
     * @return 转换后的字节数组 
     **/  
    public static byte[] hexStr2ByteArray(String hexString) {  
               
        hexString = hexString.toLowerCase();  
        final byte[] byteArray = new byte[hexString.length() / 2];  
        int k = 0;  
        for (int i = 0; i < byteArray.length; i++) {  
                        //因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先  
                        //将hex 转换成byte   "&" 操作为了防止负数的自动扩展  
                        // hex转换成byte 其实只占用了4位，然后把高位进行右移四位  
                        // 然后“|”操作  低四位 就能得到 两个 16进制数转换成一个byte.  
                        //  
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);  
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);  
            byteArray[i] = (byte) (high << 4 | low);  
            k += 2;  
        }  
        return byteArray;  
    }
    /** 
     * 16进制字符串转换成byte数组 
     * @param
     * @return 转换后的byte数组 
     */  
      public static byte[] hex2Byte(String hex) {  
      String digital = "0123456789abcdef";  
      hex = hex.toLowerCase();  
      char[] hex2char = hex.toCharArray();  
      byte[] bytes = new byte[hex.length() / 2];  
      int temp;  
      for (int i = 0; i < bytes.length; i++) {  
      // 其实和上面的函数是一样的 multiple 16 就是右移4位 这样就成了高4位了  
      // 然后和低四位相加， 相当于 位操作"|"   
      //相加后的数字 进行 位 "&" 操作 防止负数的自动扩展. {0xff byte最大表示数}  
          temp = digital.indexOf(hex2char[2 * i]) * 16;  
          temp += digital.indexOf(hex2char[2 * i + 1]);  
          bytes[i] = (byte) (temp & 0xff);  
      }  
      return bytes;  
  }  
      /**
       * 保留小数点后几位
      * @Title: formateRate
      * @author luojiayong
      * @param rateStr 要格式化的字符串
      * @param amount 保留的位数
      * @return
       */
      public static double formateRate(String rateStr,int amount){ 
 		 if(isStrNull(rateStr)) rateStr="0";
 		 BigDecimal bg = new BigDecimal(rateStr);
 	     return bg.setScale(amount, BigDecimal.ROUND_HALF_UP).doubleValue();
     }

	/**
	 * 返回错误信息对象
	 * @param message
	 * @return
	 */
	public static MessageResp returnErrorInfo(String message) {
		MessageResp result = new MessageResp();
		result.setResult(Boolean.FALSE.toString());
		result.setStatusCode("-50");
		result.setResultDesc(message);
		return result;
	}

	//企业用户返回corpId，系统用户返回null
	public static String getUserCorpId(AuthUserResp authUser){
		String corpId = null;
		if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType())){
			corpId = authUser.getCorpId().toString();
		}
		return corpId;
	}
	
	/**
	 * @Title: checkStringParamEmpty @Description: 检测字符串参数为空 @param @param
	 *         value @param @param name @param @throws BusinessException @return
	 *         void @author lyl @throws
	 */
	public static String checkStringParamEmptyReturn(String value, String name, Integer len, boolean canNull)
			throws Exception {
		String msg = "";
		if (!canNull && CommonUtils.isStrNull(value)) {
			msg = name + "不能为空,";
			return msg;
		}
		if (!CommonUtils.isStrNull(value) && null != len && len.intValue() > 0 && value.length() > len.intValue()) {
			msg += name + "长度不能超过" + len.intValue() + "个字符,";
		}
		return msg;
	}
	
	public static Boolean isCarNumSOEAndColor(String vehicleNumber) {
		Pattern pattern = Pattern.compile(carNumColorSOEExpress);
		Matcher matcher = pattern.matcher(vehicleNumber);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断字符串是否是乱码
	 *
	 * @param strName
	 *            字符串
	 * @return 是否是乱码
	 */
	public static boolean isMessyCode(String strName) {
		Pattern p = Pattern.compile("\\s*|t*|r*|n*");
		Matcher m = p.matcher(strName);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = ch.length;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
				}
			}
		}
		float result = count / chLength;
		if (result > 0.4) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @Title: validataIdNumPattern @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param @param idCard @param @return @return
	 *         boolean @author lyl @throws
	 */

	public static boolean validataIdNumPattern(String idCard) {
		// 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母 [1-9]\\d{13,16}[a-zA-Z0-9]{1}
		Pattern idNumPattern = Pattern.compile(idCardExpress);
		// 通过Pattern获得Matcher
		Matcher matcher = idNumPattern.matcher(idCard);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	
	
	public static boolean validataSex(String sexs) {
		Pattern idNumPattern = Pattern.compile(sex);
		Matcher matcher = idNumPattern.matcher(sexs);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	
	public static boolean validataLeve(String leve) {
		Pattern idNumPattern = Pattern.compile(cerLeve);
		Matcher matcher = idNumPattern.matcher(leve);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
		String aa = "04";
		System.out.println(validataLeve(aa));
	}
      
	/**
	 * 判断字符是否是中文
	 *
	 * @param c
	 *            字符
	 * @return 是否是中文
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 将list等分
	 * @param list
	 * @param blockSize
	 * @return
	 */
	public static <T> List<List<T>> subList(List<T> list, int blockSize) {
		List<List<T>> lists = new ArrayList<List<T>>();
		if (list != null && blockSize > 0) {
			int listSize = list.size();
			if (listSize <= blockSize) {
				lists.add(list);
				return lists;
			}
			int batchSize = listSize / blockSize;
			int remain = listSize % blockSize;
			for (int i = 0; i < batchSize; i++) {
				int fromIndex = i * blockSize;
				int toIndex = fromIndex + blockSize;
				lists.add(list.subList(fromIndex, toIndex));
			}
			if (remain > 0) {
				lists.add(list.subList(listSize - remain, listSize));
			}
		}
		return lists;
	}
}
