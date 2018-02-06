package com.cfets.ts.u.platform.model.mergemodel;


public class Commit   {
	private static final long serialVersionUID = 1245558473341912864L;
	 private String id;
	 private  String message;
	 private  String   timestamp;
	 private   Author author; 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	private static class Author {
		private String name;
		private String  email;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
	}
	 
}
