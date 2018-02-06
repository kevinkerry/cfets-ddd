/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ddd.d.platform.repository.domain;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 *
 * @author pluto
 * @description：
 * @create on 2017年2月27日
 * @history
 */
public final class Scope {

    private static final String SCOPE_TEST = "test";

    private static final String SCOPE_PROVIDED = "provided";

    private static final String SCOPE_COMPILE = "compile";

    private static final String SCOPE_SYSTEM = "system";

    private static final String SCOPE_EXTEND = "extend";

    private static final String SCOPE_BELONG = "belong";

    private static Map<String, Scope> scopeMap = new HashMap<>();


    static {
        scopeMap.put(SCOPE_TEST, new Scope(SCOPE_TEST));
        scopeMap.put(SCOPE_PROVIDED, new Scope(SCOPE_PROVIDED));
        scopeMap.put(SCOPE_COMPILE, new Scope(SCOPE_COMPILE));
        scopeMap.put(SCOPE_SYSTEM, new Scope(SCOPE_SYSTEM));
        scopeMap.put(SCOPE_EXTEND, new Scope(SCOPE_EXTEND));
        scopeMap.put(SCOPE_BELONG, new Scope(SCOPE_BELONG));
    }

    private String name;

    Scope(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scope scope = (Scope) o;

        return name != null ? name.equals(scope.name) : scope.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public static Scope obtain(String content) {
        if (StringUtils.isNotBlank(content) && scopeMap.containsKey(content)) {
            return scopeMap.get(content);
        } else {
            return scopeMap.get(SCOPE_COMPILE);
        }
    }

}
