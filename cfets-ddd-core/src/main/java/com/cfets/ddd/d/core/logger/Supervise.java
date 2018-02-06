package com.cfets.ddd.d.core.logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by pluto on 22/01/2018.
 */
public interface Supervise {

    /**
     * 根据异常类获取错误码
     *
     * @return
     */
    public String getErrorCode();

    /**
     * 根据异常key，获取错误码
     *
     * @param key
     * @return
     */
    public String getErrorCode(String key);

    /**
     * 根据语言,获取错误信息
     *
     * @param language
     * @return
     */
    public String getErrorInfo(String language);

    /**
     * 根据异常key和语言,获取错误信息
     *
     * @param key
     * @param language
     * @return
     */
    public String getErrorInfo(String key, String language);

    /**
     * 获取错误信息
     *
     * @param request
     * @return
     */
    public String getErrorInfo(HttpServletRequest request);

    /**
     * 获取错误信息
     *
     * @param key
     * @param request
     * @return
     */
    public String getErrorInfo(String key, HttpServletRequest request);

    /**
     * 设置key
     *
     * @param key
     * @return
     */
    public Supervise key(String key);

    /**
     * 获取key
     *
     * @return
     */
    public String getKey();

    /**
     * 获取占位符
     *
     * @return
     */
    public Object[] getArgs();

    /**
     * 是否存在占位符
     * @return
     */
    public boolean hasArgs();
}
