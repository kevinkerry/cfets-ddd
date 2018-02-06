package com.cfets.ddd.d.core.logger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;

/**
 * Created by pluto on 22/01/2018.
 */
public class DLogger {

    private Class<?> toBeLoggerClass;

    final static String FQCN = DLogger.class.getName();

    final Logger logger;// Log文件日志记录器

    protected static boolean debugWithPrinting = false;

    public DLogger(Class<?> toBeLoggerClass) {
        this.toBeLoggerClass = toBeLoggerClass;
        logger = LoggerFactory.getLogger(toBeLoggerClass);

    }

    public static DLogger getLogger(Class<?> toBeClass) {
        return new DLogger(toBeClass);
    }

    protected static void changeDebug(boolean debugWithPrinting) {
        DLogger.debugWithPrinting = debugWithPrinting;
    }

    protected static boolean isDebugPrinting() {
        return DLogger.debugWithPrinting;
    }

    // /////////////////////////////////本地日志////////////////////////////////////////////
    public boolean isDebugEnabled() {
        if (DLogger.debugWithPrinting) {
            return true;
        } else {
            return logger.isDebugEnabled();
        }
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public void debug(String message, Object... parameters) {
        if (DLogger.debugWithPrinting) {
            logger.info(message);
        } else {
            if (isDebugEnabled()) {
                logger.debug(message, parameters);
            }
        }
    }

    public void debug(String message) {
        if (DLogger.debugWithPrinting) {
            logger.info(message);
        } else {
            if (isDebugEnabled()) {
                logger.debug(message);
            }
        }
    }

    public void info(String message, Object... parameters) {
        if (isInfoEnabled()) {
            logger.info(message, parameters);
        }
    }

    public void info(String message) {
        if (isInfoEnabled()) {
            logger.info(message);
        }
    }

    /**
     * 该异常主要用于输出不影响程序进行的异常信息
     *
     * @param
     * @return void
     * @throws
     * @description:[info]
     * @author pluto 2017年2月15日
     * @since 0.1.8.0
     */
    public void info(Supervise e) {
        if (isInfoEnabled()) {
            String msg = getSuperviseMessage(e, null);
            logger.info(msg);
        }
    }

    public void warn(String message) {
        if (isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public void warn(Throwable e) {
        if (isWarnEnabled()) {
            logger.warn(e.getMessage(), e);
        }
    }

    public void warn(String message, Object... parameters) {
        if (isWarnEnabled()) {
            logger.warn(message, parameters);
        }
    }

    public void warn(String message, Throwable e) {
        if (isWarnEnabled()) {
            logger.warn(message, e);
        }
    }

    public void warn(String message, Throwable e, Object... parameters) {
        if (isWarnEnabled()) {
            String msg = format(message, parameters);
            logger.warn(msg, e);
        }
    }

    public void error(String message) {
        if (isErrorEnabled()) {
            logger.error(message);
        }
    }

    public void error(Throwable e) {
        if (isErrorEnabled()) {
            logger.error(e.getMessage(), e);
        }
    }

    public void error(Throwable e, Supervise s) {
        if (isErrorEnabled()) {
            String msg = getSuperviseMessage(s, null);
            logger.error(msg, e);
        }
    }

    public void error(String message, Throwable e) {
        if (isErrorEnabled()) {
            logger.error(message, e);
        }
    }

    public void error(String message, Supervise e) {
        if (isErrorEnabled()) {
            String msg = getSuperviseMessage(e, null) + " " + message;
            Throwable t = (Throwable) e;
            logger.error(msg, t);
        }
    }

    public void error(String message, Object... parameters) {
        if (isErrorEnabled()) {
            logger.error(message, parameters);
        }
    }

    public void error(String message, Throwable e, Object... parameters) {
        if (isErrorEnabled()) {
            String msg = format(message, parameters);
            logger.error(msg, e);
        }
    }


    // /////////////////////////////////本地日志////////////////////////////////////////////


    private static String format(String message, Object... parameters) {
        try {
            FormattingTuple ft = MessageFormatter.arrayFormat(message,
                    parameters);
            return ft.getMessage();
        } catch (Exception e) {
            String error = "传入参数包含特殊字符，导致日志格式化时发生异常，对应用本身无影响：" + e.getMessage()
                    + "\t" + Arrays.toString(parameters);
            return error;
        }
    }

    public Class<?> getToBeLoggerClass() {
        return toBeLoggerClass;
    }

    private String getSuperviseMessage(Supervise s, String language) {
        if (StringUtils.isBlank(language)) {
            language = "en-US";
        }
        String msg = "[" + s.getErrorCode() + "]" + s.getErrorInfo(language);
        return msg;
    }

}
