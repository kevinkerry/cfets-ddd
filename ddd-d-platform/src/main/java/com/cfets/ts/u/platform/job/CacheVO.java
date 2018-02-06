package com.cfets.ts.u.platform.job;

import java.io.Serializable;

public class CacheVO  implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -7693043185037438991L;
private String groupId;
private String artifactId;
private String version;
private String branch;
public String getGroupId() {
	return groupId;
}
public String getArtifactId() {
	return artifactId;
}
public String getVersion() {
	return version;
}
public String getBranch() {
	return branch;
}
public void setGroupId(String groupId) {
	this.groupId = groupId;
}
public void setArtifactId(String artifactId) {
	this.artifactId = artifactId;
}
public void setVersion(String version) {
	this.version = version;
}
public void setBranch(String branch) {
	this.branch = branch;
}
@Override
public String toString() {
	return "CacheVO [groupId=" + groupId + ", artifactId=" + artifactId
			+ ", version=" + version + ", branch=" + branch + "]";
}

}
