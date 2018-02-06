package com.cfets.ts.u.platform.model.mergemodel;

import java.io.Serializable;

public class MergeObejectEntity implements Serializable{
	private static final long serialVersionUID = 1634419046800427283L;
private String id;
private String target_branch;
private String source_branch;
private int source_project_id;
private  int  target_project_id;
private int author_id;
private int assignee_id;

private String title;
private String description;
private String  created_at;
private String  updated_at;
private String state;//meijuzhi
private String merge_status;//meijuzhi
private Source source;
private Target target; 
private Commit last_commit;
private int iid;  


public int getIid() {
	return iid;
}

public void setIid(int iid) {
	this.iid = iid;
}

public Target getTarget() {
	return target;
}

public void setTarget(Target target) {
	this.target = target;
}

public Commit getLast_commit() {
	return last_commit;
}

public void setLast_commit(Commit last_commit) {
	this.last_commit = last_commit;
}

public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}

public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}

public String getTarget_branch() {
	return target_branch;
}

public void setTarget_branch(String target_branch) {
	this.target_branch = target_branch;
}

public String getSource_branch() {
	return source_branch;
}

public void setSource_branch(String source_branch) {
	this.source_branch = source_branch;
}

public String getCreated_at() {
	return created_at;
}

public void setCreated_at(String created_at) {
	this.created_at = created_at;
}

public String getUpdated_at() {
	return updated_at;
}

public void setUpdated_at(String updated_at) {
	this.updated_at = updated_at;
}

public String getState() {
	return state;
}

public void setState(String state) {
	this.state = state;
}

public String getMerge_status() {
	return merge_status;
}

public void setMerge_status(String merge_status) {
	this.merge_status = merge_status;
}

public Source getSource() {
	return source;
}

public void setSource(Source source) {
	this.source = source;
}

//源头
public static class Source{
	private String name;
	private String description;
	private  String web_url;
	private String avatar_url;
	private String namespace;
	private String default_branch;
	private String  homepage;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getWeb_url() {
		return web_url;
	}
	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getDefault_branch() {
		return default_branch;
	}
	public void setDefault_branch(String default_branch) {
		this.default_branch = default_branch;
	}
}
//还有属性todo
public static class Target  {
	private String name;
	private String description;
	private String web_url;
	private String avatar_url;
	private String git_ssh_url;
	private String git_http_url;
	private String namespace;
	private String visibility_level;
	private String path_with_namespace;
	private String default_branch;
	private String homepage;
	private String url;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getWeb_url() {
		return web_url;
	}
	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getGit_ssh_url() {
		return git_ssh_url;
	}
	public void setGit_ssh_url(String git_ssh_url) {
		this.git_ssh_url = git_ssh_url;
	}
	public String getGit_http_url() {
		return git_http_url;
	}
	public void setGit_http_url(String git_http_url) {
		this.git_http_url = git_http_url;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getVisibility_level() {
		return visibility_level;
	}
	public void setVisibility_level(String visibility_level) {
		this.visibility_level = visibility_level;
	}
	public String getPath_with_namespace() {
		return path_with_namespace;
	}
	public void setPath_with_namespace(String path_with_namespace) {
		this.path_with_namespace = path_with_namespace;
	}
	public String getDefault_branch() {
		return default_branch;
	}
	public void setDefault_branch(String default_branch) {
		this.default_branch = default_branch;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
	
	//规则判断是否符合提交要求
	
	
    public int getSource_project_id() {
		return source_project_id;
	}


	public void setSource_project_id(int source_project_id) {
		this.source_project_id = source_project_id;
	}


	public int getTarget_project_id() {
		return target_project_id;
	}


	public void setTarget_project_id(int target_project_id) {
		this.target_project_id = target_project_id;
	}


	public int getAuthor_id() {
		return author_id;
	}


	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}


	public int getAssignee_id() {
		return assignee_id;
	}


	public void setAssignee_id(int assignee_id) {
		this.assignee_id = assignee_id;
	}
}
