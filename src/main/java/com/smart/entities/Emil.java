package com.smart.entities;

import java.io.File;

public class Emil {
	
	private String to;
	private String name;
	private String email;
	private String subject;
	private String content;
	private File file;
	public Emil() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Emil(String name, String email, String content, File file) {
		super();
		this.name = name;
		this.email = email;
		this.content = content;
		this.file = file;
	}
	

	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	

}
