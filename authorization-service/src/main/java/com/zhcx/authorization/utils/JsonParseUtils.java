package com.zhcx.authorization.utils;

import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json和javaBean转换工具类
 * 
 * @title
 * @author 龚进
 * @date 2018年11月28日
 * @version 1.0
 */
public class JsonParseUtils {

	private static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	}

	/**
	 * 把json转成javaBean
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T json2Bean(String json, Class<T> clazz) throws Exception {
		T bean = objectMapper.readValue(json, clazz);
		return bean;
	}
	
	/**
	 * 把json转为list
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> json2List(String json, Class<T> clazz) throws Exception {
		List<T> listBean = objectMapper.readValue(json, new TypeReference<List<T>>() {});
		return listBean;
	}

	/**
	 * 把javaBean转成json
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static <T> String bean2Json(T bean) throws Exception {
		String json = objectMapper.writeValueAsString(bean);
		return json;
	}

	public static void main(String[] args) throws Exception {
//		KafkaMessage km = new KafkaMessage();
//		List<KafkaContent> list = Lists.newArrayList();
//		KafkaContent kc = new KafkaContent();
//		kc.setDataId("1");
//		kc.setContent("测试消息1");
//		list.add(kc);
//		km.setType("device");
//		km.setContents(list);
//		String json = bean2Json(km);
//		System.out.println(json);
//		KafkaMessage km1 = json2Bean(json, KafkaMessage.class);
//		System.out.println(km1.toString());
//		String jsonList = bean2Json(list);
//		System.out.println(jsonList);
//		list = json2List(jsonList, KafkaContent.class);
//		System.out.println(list);
//		
//		System.out.println(TopicNamespaceEnum.getNameByValue("wisdombus2.0_bus"));
	}
}
