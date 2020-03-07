package com.crygrade.models;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class User {
	private String uid;
	private String role;
	private String firstName;
	private String lastName;
	private String userClass;
	private HashMap<String, Grade> grades;

	public User() { }

	@NonNull
	@Override
	public String toString() {
		return firstName + " " + lastName + (userClass != null ? ", " + userClass :  "");
	}

	public User(String role) {
		this.role = role;
	}

	public User(String role, String firstName, String lastName) {
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public User(String role, String firstName, String lastName, String userClass) {
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userClass = userClass;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	public String getUid() {
		return uid;
	}

	public User setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public HashMap<String, Grade> getGrades() {
		return grades;
	}

	public void setGrades(HashMap<String, Grade> grades) {
		this.grades = grades;
	}
}
