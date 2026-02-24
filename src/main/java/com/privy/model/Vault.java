package com.privy.model;

public class Vault {
	
	private int id;
	private String urlName;
	private String url;
	private String userName;
	private String password;
	private int login_id;
	

	public Vault(int id, String urlName, String url , String userName, String password, int login_id) {
		super();
		this.id = id;
		this.url = url;
		this.urlName = urlName;
		this.userName = userName;
		this.password = password;
		this.login_id = login_id;
	}
	
	// Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrlName() {
		return urlName;
	}
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getLogin_id() {
		return login_id;
	}
	public void setLogin_id(int login_id) {
		this.login_id = login_id;
	}
	
	
}
