package de.predbo.vertx.model;

import java.util.concurrent.atomic.AtomicInteger;

public class User {

	private static final AtomicInteger COUNTER = new AtomicInteger();

	private final int _id;
	private String _name;
	private String _lastname;

	public User(String name, String lastname) {
		_id = COUNTER.getAndIncrement();
		_name = name;
		_lastname = lastname;
	}

	public User() {
		_id = COUNTER.getAndIncrement();
	}

	public String getName() {
		return _name;
	}

	public String getLastName() {
		return _lastname;
	}

	public int getId() {
		return _id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setLastname(String origin) {
		_lastname = origin;
	}
}
