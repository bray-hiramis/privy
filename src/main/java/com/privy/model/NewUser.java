package com.privy.model;

public class NewUser {

	private String userName;
	private String password;
	private String email;
	private String securityQuestion;
	private String answer;
	
	public NewUser(String userName, String password, String email, String securityQuestion, String answer) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.securityQuestion = securityQuestion;
		this.answer = answer;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
