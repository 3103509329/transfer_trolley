package com.zhcx.authorization.config;

import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.common.util.UUIDUtils;
import com.zhcx.netcarbasic.facade.base.OperationLogService;
import com.zhcx.netcarbasic.pojo.base.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/10/26 17:20
 **/
@Aspect
@Component
@Slf4j
public class SystemLogAspect {

    @Autowired
    private UUIDUtils uuidUtils;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @Pointcut("@annotation(com.zhcx.authorization.config.SystemControllerLog)")
    public void controllerAspect(){
    }

    /**
     * @Description  前置通知  用于拦截Controller层记录用户的操作
     * @date 2018年9月3日 10:38
     */
    @After("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try{
            String url = request.getRequestURI();
            //操作内容
            String operation = getControllerMethodDescription(joinPoint);
            operation = operation + "{url:"+url+",param:[";
            Enumeration enu=request.getParameterNames();
            while(enu.hasMoreElements()){
                String paraName=(String)enu.nextElement();
                if (!StringUtils.equals("password", paraName)) {
                    operation = operation + paraName + ":"+request.getParameter(paraName)+",";
                }
            }
            operation = operation +"]}";

            String logType = getControllerMethodActionType(joinPoint);

            //ip
            String ip = getIpAddr(request);

            //用户id
            long operatorId = 0L;
            //用户名
            String operatorName = "无";
            AuthUserResp user = sessionHandler.getUser(request);
            if(null != user && null != user.getUserId()){
                operatorId = user.getUserId();
                operatorName = user.getAccountName();
            }

            //备注
            String remark = "";

            //目标对象
            long targetId = 0L;

            //对象名称
            String targetName = "";

            //来源
            String source = "";

            OperationLog operationLog = new OperationLog();
            operationLog.setUuid(uuidUtils.getLongUUID("operation_log"));
            operationLog.setOperatorId(operatorId);
            operationLog.setOperatorName(operatorName);
            if (StringUtils.equals("无", operatorName)) {
                operationLog.setOperation("登录失败");
            } else {
                operationLog.setOperation(operation);
            }
            operationLog.setLoginIp(ip);
            operationLog.setLogType(logType);
            operationLog.setRemark(remark);
            operationLog.setTargetId(targetId);
            operationLog.setTargetName(targetName);
            operationLog.setSource(source);
            operationLog.setUrl(url);
            operationLogService.addOperationLog(operationLog);

        }catch (Exception e){
            log.error("添加登录日志异常:");
            log.error("异常信息：{}",e.getMessage());
        }
    }


    /**
     * @author changyaofang
     * @Description  获取注解中对方法的描述信息 用于Controller层注解
     * @date 2018年9月3日 上午12:01
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * @author changyaofang
     * @Description  获取注解中对方法的描述信息 用于Controller层注解
     * @date 2018年9月3日 上午12:01
     */
    public static String getControllerMethodActionType(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String actionType = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    actionType = method.getAnnotation(SystemControllerLog.class).actionType();
                    break;
                }
            }
        }
        return actionType;
    }

    public String getIpAddr(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");

        String ip = null;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if(forwarded != null){
                    forwarded = forwarded.split(",")[0];
                }
                ip = realIp + "/" + forwarded;
            }
        }
        return ip;
    }

}
