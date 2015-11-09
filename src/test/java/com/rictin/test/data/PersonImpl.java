package com.rictin.test.data;

import java.util.concurrent.atomic.AtomicInteger;

public class PersonImpl implements Person {

	private String name;
	private int age;
	private Long points;
	private Double score;

	public PersonImpl(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	public AtomicInteger getAtomicInteger() {
		return new AtomicInteger(4);
	}

	@Override
	public String toString() {
		return name + "(" + age + ")";
	}

}
