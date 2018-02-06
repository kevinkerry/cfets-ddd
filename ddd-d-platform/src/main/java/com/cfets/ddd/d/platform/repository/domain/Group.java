/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ddd.d.platform.repository.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class Group {

	private Map<String, Component> nodes = new HashMap<String, Component>();

	private List<Relation> relations = new ArrayList<Relation>();

}
