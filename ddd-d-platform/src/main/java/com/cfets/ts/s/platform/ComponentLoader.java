/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform;

import com.cfets.ddd.d.platform.repository.domain.ComponentGroup;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年2月27日
 * 
 * @history
 * 
 */
public interface ComponentLoader {

	ComponentGroup load(ComponentGroup componentGroup);

}
