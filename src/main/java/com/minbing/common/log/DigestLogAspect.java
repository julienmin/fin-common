package com.minbing.common.log;

import com.google.common.collect.Maps;
import com.minbing.common.lang.FinException;
import com.minbing.common.lang.FinResult;
import com.minbing.common.utils.GroovyUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class DigestLogAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(DigestLogAspect.class);

    private final static Map<String, Logger> LOGGER_CACHE = Maps.newConcurrentMap();

    @Around(value = "@annotation(digestLog)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint, DigestLog digestLog) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        Throwable exception = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {

        }
        return result;
    }

    private void logDigest(JoinPoint joinPoint, DigestLog digestLog, Object ret, long costTime, Throwable exception) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        try {
            Map<String, Object> context = buildContext(joinPoint, ret);
            String expression = GroovyUtil.renderTemplateStr(digestLog.expression(), context);

            // TODO
            String traceId = "";
            String clientIp = "";
            String tenantId = "";
            String productCode = "";
            String subProductCode = "";
            String eventCode = "";
            String legalEntityId = "";

            String classAndMethodd = className + "#" + methodName;

            ResultInfo resultInfo = buildResultInfo(ret, exception);

            // 获取摘要日志logger
            Logger digestLogger = getLogger(digestLog.appender());
            digestLogger.info(DigestLogConstants.DIGEST_FORMAT,
                    traceId, clientIp, tenantId, productCode, subProductCode, eventCode, legalEntityId,
                    classAndMethodd,
                    defaultIfBlank(resultInfo.getResultCode()), defaultIfBlank(resultInfo.getErrorCode()), costTime,
                    expression,
                    defaultIfBlank(resultInfo.getErrorMessage())
            );
        } catch (Exception e) {
            LOGGER.warn("Log digest log error, className={}, method={}", className, methodName);
        }
    }

    private String defaultIfBlank(String str) {
        return StringUtils.isBlank(str) ? "" : str;
    }

    @Getter
    @Setter
    class ResultInfo {
        private String resultCode;

        private String errorCode;

        private String errorMessage;

        public ResultInfo() {

        }

        public ResultInfo(String resultCode, String errorCode, String errorMessage) {
            this.resultCode = resultCode;
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }
    }

    private ResultInfo buildResultInfo(Object ret, Throwable exception) {
        if (ret != null && ret instanceof FinResult) {
            FinResult finResult = (FinResult) ret;
            return new ResultInfo(finResult.getResultCode().name(), finResult.getErrorCode(), finResult.getErrorMessage());
        }

        if (exception != null) {
            if (exception instanceof FinException) {
                FinException finException = (FinException) exception;
                return new ResultInfo(null, finException.getErrorCode().getCode(), finException.getMessage());
            } else if (exception.getCause() instanceof FinException) {
                FinException finException = (FinException) exception.getCause();
                return new ResultInfo(null, finException.getErrorCode().getCode(), finException.getMessage());
            }
        }

        return new ResultInfo();
    }

    private static Map<String, Object> buildContext(JoinPoint joinPoint, Object ret) {
        Map<String, Object> context = buildArgsMap(joinPoint);

        context.put("ret", ret);

        return context;
    }

    private static Map<String, Object> buildArgsMap(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        Map<String, Object> argsMap = Maps.newHashMap();
        if (args == null || argNames == null || argNames.length < 1 || args.length != argNames.length) {
            return argsMap;
        }

        for (int i = 0; i < args.length; i++) {
            argsMap.put(argNames[i], args[i]);
        }

        return argsMap;
    }

    private Logger getLogger(String appenderName) {
        Logger logger = LOGGER_CACHE.get(appenderName);
        if (logger == null) {
            logger = LoggerFactory.getLogger(appenderName);
            LOGGER_CACHE.put(appenderName, logger);
        }
        return logger;
    }
}
