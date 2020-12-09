package com.zhcx.authorization.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
/**
 * @ClassName：BeanUtils
 * @Description:bean扭转  如果value为null 则不copy
 * @author：luoyalan
 * @date：2015年12月2日 下午4:01:32 
 * @version
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

	public static void copyProperties(Object source, Object target) throws BeansException {
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					String msg = sourcePd.getName();
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						
						msg = msg + ":" + value;
						
						// 这里判断以下value是否为空
						if (value != null) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							Class<?>[] writeClasses = writeMethod.getParameterTypes();
							Class<?> writeParameterClazz = writeClasses[0];
							
							Class<?> readParameterClazz = readMethod.getReturnType();
							if(!readParameterClazz.getCanonicalName().equals(String.class.getCanonicalName()) && writeParameterClazz.getCanonicalName().equals(String.class.getCanonicalName())){
								writeMethod.invoke(target, value.toString());
							}else{
								writeMethod.invoke(target, value);
							}
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target[" + msg + "]", ex);
					}
				}
			}
		}
	}
	
	public static void copyBasicProperties(Object source, Object target) throws BeansException {
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					String msg = sourcePd.getName();
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						
						msg = msg + ":" + value;
						
						// 这里判断以下value是否为空
						if (value != null) {
							String objType = value.getClass().getName();
							if(objType.startsWith("java")){
								Method writeMethod = targetPd.getWriteMethod();
								if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
									writeMethod.setAccessible(true);
								}
								writeMethod.invoke(target, value);
							}
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target[" + msg + "]", ex);
					}
				}
			}
		}
	}
	
	public static void copyProperties(Object source, Object target, String[] ignoreProperties) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;

		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null &&
					(ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
							// 这里判断以下value是否为空
							if (value != null) {
								Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					}
					catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}
	

	public static void copyPropertiesAll(Object source, Object target) throws BeansException {
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					String msg = sourcePd.getName();
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						
						msg = msg + ":" + value;
						
						// 这里判断以下value是否为空
//						if (value != null) {
						String objType = value.getClass().getName();
						if(objType.startsWith("java")){
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value == null ? "" : value);
						}
//						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target[" + msg + "]", ex);
					}
				}
			}
		}
	}
}
