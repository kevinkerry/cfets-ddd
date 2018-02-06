package com.cfets.ts.u.platform.model.mergemodel;

import java.io.Serializable;

public class Project  implements Serializable{
	private static final long serialVersionUID = 7046262208700107276L;
private String name;
private String description;
private String namespace;

//and so on
public String getname() {
	return name;
}
public void setname(String name) {
	this.name = name;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getNamespace() {
	return namespace;
}
public void setNamespace(String namespace) {
	this.namespace = namespace;
}

}
