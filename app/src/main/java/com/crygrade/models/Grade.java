package com.crygrade.models;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Grade {
	private Integer grade;
	private String description;
	private Integer weight;
	private Long date;
	private String category;
	private String creator;
	private String subject;

	public Grade() { }

	public Grade(Integer grade, Integer weight, String category, String description) {
		this.grade = grade;
		this.weight = weight;
		this.category = category;
		this.description = description;
	}

	@NonNull
	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "Ocena: %d\nWaga: %d\nKategoria: %s\nOpis: %s",
				getGrade(),
				getWeight(),
				getCategory(),
				getDescription()
		);
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
