/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ddd.d.platform.repository.domain;

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
public class Relation implements java.io.Serializable {

	private static final long serialVersionUID = -124533364144158695L;

	private Component one;

	private Component another;

	private int degree;

	private Scope scope;

	public Component getOne() {
		return one;
	}

	public void setOne(Component one) {
		this.one = one;
	}

	public Component getAnother() {
		return another;
	}

	public void setAnother(Component another) {
		this.another = another;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public static Relation obtain(Component one, Component another, Scope scope) {
		Relation relation = new Relation();
		relation.setOne(one);
		relation.setAnother(another);
		relation.setScope(scope);
		return relation;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("one");
		sb.append(one.toString());
		sb.append("another");
		sb.append(another.toString());
		sb.append("scope[");
		sb.append(scope.getName());
		sb.append("]");
		return sb.toString();
	}

}
