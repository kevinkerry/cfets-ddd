package com.cfets.ts.u.platform.bean;

import java.io.Serializable;

/**
 * name":"谢圆良", "username":"xieyuanliang_zh", "id":305, "state":"active",
 * "avatar_url":
 * "http://www.gravatar.com/avatar/572602fa9d75b867a0d07a251a0b0b75?s=80\u0026d=identicon"
 * , "web_url":"http://gitlab.scm.cfets.com/u/xieyuanliang_zh",
 * "access_level":30
 * 
 * @author zrp
 * 
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1856376647764055254L;
	private Integer id;
	private String name;
	private String username;
	private String state;
	private String avatar_url;
	private String access_level;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getAccess_level() {
		return access_level;
	}
	public void setAccess_level(String access_level) {
		this.access_level = access_level;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username
				+ ", state=" + state + ", avatar_url=" + avatar_url
				+ ", access_level=" + access_level + "]";
	}

	
}
