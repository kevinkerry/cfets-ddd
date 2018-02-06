package com.cfets.ts.s.platform.bean;

import java.io.Serializable;

public class SourceOrTarget implements Serializable {

	private static final long serialVersionUID = 7073369135926027714L;
	
private String source;
private String target;
public String getSource() {
	return source;
}
public void setSource(String source) {
	this.source = source;
}
public String getTarget() {
	return target;
}
public void setTarget(String target) {
	this.target = target;
}



}
