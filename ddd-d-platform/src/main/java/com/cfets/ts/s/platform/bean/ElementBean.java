package com.cfets.ts.s.platform.bean;

import org.xml.sax.helpers.AttributesImpl;

public class ElementBean {

	public static final int START_ELEMENT = 1;

	public static final int CHARACTERS = 2;

	public static final int END_ELEMENT = 3;

	private String value;

	private AttributesImpl attributes;

	private int elementType;

	public ElementBean() {
		super();
	}

	public ElementBean(String value, AttributesImpl attributes, int elementType) {
		super();
		this.value = value;
		this.attributes = attributes;
		this.elementType = elementType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public AttributesImpl getAttributes() {
		return attributes;
	}

	public void setAttributes(AttributesImpl attributes) {
		this.attributes = attributes;
	}

	public int getElementType() {
		return elementType;
	}

	public void setElementType(int elementType) {
		this.elementType = elementType;
	}

	@Override
	public String toString() {
		return "ElementBean [value=" + value + ", attributes=" + attributes + ", elementType=" + elementType + "]";
	}

}
