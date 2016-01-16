package de.predbo.vertx;

import java.util.LinkedHashMap;
import java.util.Map;

import de.predbo.vertx.model.User;

public class UserRegistry {

	private Map<Integer, User> _registry = new LinkedHashMap<>();

	protected void createSampleRegistry() {
		User hugo = new User("Hugo", "Müller");
		getUsers().put(hugo.getId(), hugo);
		User berta = new User("Berta", "Schmidt");
		getUsers().put(berta.getId(), berta);
	}

	public Map<Integer, User> getUsers() {
		return _registry;
	}

}
