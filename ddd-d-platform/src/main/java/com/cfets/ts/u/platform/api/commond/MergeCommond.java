package com.cfets.ts.u.platform.api.commond;

import java.io.Serializable;

import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity;
import com.cfets.ts.u.platform.model.mergemodel.Project;
import com.cfets.ts.u.platform.model.mergemodel.User;

public class MergeCommond implements Serializable{
	private static final long serialVersionUID = 6239573047560247289L;
	private String object_kind;
	private MergeObejectEntity object_attributes;
	private Project project;
	private User user;
	
	public MergeCommond() {
		// TODO Auto-generated constructor stub
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getObject_kind() {
		return object_kind;
	}
	public void setObject_kind(String object_kind) {
		this.object_kind = object_kind;
	}
	public MergeObejectEntity getObject_attributes() {
		return object_attributes;
	}
	public void setObject_attributes(MergeObejectEntity object_attributes) {
		this.object_attributes = object_attributes;
	}
   
}
