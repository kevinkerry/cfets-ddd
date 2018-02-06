package com.cfets.ts.u.platform;

public class MavenInstallException extends Exception {
private String msg;

public String getMsg() {
	return msg;
}

public void setMsg(String msg) {
	this.msg = msg;
}

public MavenInstallException(String msg) {
	super();
	this.msg = msg;
};

}
