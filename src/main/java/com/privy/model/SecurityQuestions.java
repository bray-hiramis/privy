package com.privy.model;

public class SecurityQuestions {

	private int id;
	private String securityQuestion;
	
	public SecurityQuestions(int id, String secQuestions) {
		this.id = id;
		this.securityQuestion = secQuestions;
	}
	
	public String getSecurityQuestions() {
		return securityQuestion;
	}
	
	public void setSecurityQuestions(String secQuestions) {
		this.securityQuestion = secQuestions;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
}
