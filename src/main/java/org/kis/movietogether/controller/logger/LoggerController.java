package org.kis.movietogether.controller.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Controller
public class LoggerController {

    @After("within(@Loggable *)")
    @SuppressWarnings("java:S1312")
    public void logPublicMethods(final JoinPoint point) {
        final CodeSignature codeSignature = (CodeSignature) point.getSignature();
        final MethodSignature methodSignature = (MethodSignature) point.getSignature();
        final Method method = methodSignature.getMethod();
        final Class<?> declaringClass = method.getDeclaringClass();
        final Loggable annotation = declaringClass.getAnnotation(Loggable.class);

        final Logger logger = LoggerFactory.getLogger(declaringClass);
        final LogLevel logLevel = annotation.logLevel();

        String methodName = method.getName();
        Object[] methodArgs = point.getArgs();
        String[] methodParams = codeSignature.getParameterNames();
        log(logger, logLevel, createLogEntry(methodName, methodParams, methodArgs));
    }

    private static String createLogEntry(final String methodName,final String[] params,final Object[] args) {
        final StringBuilder message = new StringBuilder()
                .append("Method called: ").append(methodName).append("(");
        if (Objects.nonNull(params) && Objects.nonNull(args) && params.length == args.length) {
            Map<String, Object> values = new HashMap<>(params.length);
            for (int i = 0; i < params.length; i++) {
                values.put(params[i], args[i]);
            }
            message.append(values);
        }
        return message.append(")").toString();
    }

    @SuppressWarnings("java:S131")
    private static void log(final Logger logger,final LogLevel level,final String message) {
        switch (level) {
            case TRACE -> logger.trace(message);
            case DEBUG -> logger.debug(message);
            case INFO -> logger.info(message);
            case WARN -> logger.warn(message);
            case ERROR, FATAL -> logger.error(message);
        }
    }
}
