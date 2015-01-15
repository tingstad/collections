package com.rictin.test;

public class Document {

	private String title;	
	private int views;
	
	public Document(String title) {
		this.title = title;
	}

	public String getTitle() {
		views++;
		return title;
	}

	public int getViews() {
		return views;
	}

	@Override
	public String toString() {
		return title;
	}
	
}
