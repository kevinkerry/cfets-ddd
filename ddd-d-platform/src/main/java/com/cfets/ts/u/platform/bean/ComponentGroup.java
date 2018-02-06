/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.u.platform.bean;

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
public class ComponentGroup {

	private Map<String, Component> components = new HashMap<String, Component>();

	private List<Relation> relations = new ArrayList<Relation>();

	public void registerComponent(Component component) {
		components.put(component.toString(), component);
	}

	public void addRelation(Relation relation) {
		relations.add(relation);
	}

	public void addAllRelation(List<Relation> relations) {
		this.relations.addAll(relations);
	}

	public List<Component> getAllComponentDependencied(String name) {
		List<Component> list = new ArrayList<Component>();
		Component obj = components.get(name);
		list.add(obj);
		for (Relation relation : relations) {
//			if (obj.getName().equals(relation.getOne().getName())) {
//				list.add(relation.getAnother());
//			}
			if (obj.getName().equals(relation.getAnother().getName())) {
				list.add(relation.getOne());
			}
		}
		return list;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(components.values().toString());
		sb.append("\n");
		sb.append(relations.toString());
		return sb.toString();
	}

}
