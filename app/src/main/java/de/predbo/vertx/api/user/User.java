package de.predbo.vertx.api.user;


public class User {

	private int _id;
	private String _name;
	private String _lastname;
	private String _password;

	public User(String name, String lastname, String password) {
		_name = name;
		_lastname = lastname;
		_password = password;
	}

	public User() {
		// DefaultConstructor needed for JSON Serialization/Mapping
	}

	public String getName() {
		return _name;
	}

	public String getLastname() {
		return _lastname;
	}

	public int getId() {
		return _id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setLastname(String lastname) {
		_lastname = lastname;
	}
	
	public void setId(int id) {
		_id = id;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String _password) {
		this._password = _password;
	}
}
