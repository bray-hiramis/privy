package com.privy.model;

public class User {

	private int id;
	private String userName;
	
	public User(int id, String username) {
		this.id = id;
		this.userName = username;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
}
