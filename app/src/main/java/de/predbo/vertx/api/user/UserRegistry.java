package de.predbo.vertx.api.user;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRegistry {

	private static final Logger _logger = LoggerFactory.getLogger(UserRegistry.class);
	private static final AtomicInteger USER_IDS = new AtomicInteger(0);
	
	private final Map<Integer, User> _registry = new LinkedHashMap<>();

	public UserRegistry() {
		reset();
	}
	
	void reset() {
		_registry.clear();
		USER_IDS.set(0);
		registerUser(new User("Hugo", "Müller", "geheim"));
		registerUser(new User("Berta", "Schmidt", "geheim"));
		registerUser(new User("Max", "Kruse", "geheim"));
	}

	public User getUserByName(String name) {
		for (Entry<Integer, User> registryEntry : _registry.entrySet()) {
			if (registryEntry.getValue().getName().equalsIgnoreCase(name)) {
				return registryEntry.getValue();
			}
		}
		return null;
	}
	
	Map<Integer, User> getUsers() {
		return _registry;
	}
	
	User getUser(Integer id) {
		return _registry.get(id);
	}

	void registerUser(User user) {
		user.setId(USER_IDS.incrementAndGet());
		_registry.put(user.getId(), user);
	}
	
	void unregisterUser(Integer id) {
		_registry.remove(id);
	}
	
	void updateUser(Integer id, User user) {
		_registry.put(id, user);
	}
	
	User getUserByUnvalidatedId(String idString)  {
		User user = null;
		_logger.info(" try to saftly get user with id " + idString);
		try {
			user = getUser(Integer.parseInt(idString));
		} catch (Exception e) {
			_logger.warn(String.format("The received id '%s' seems to be not an valid integer. Error: '%s'", idString, e.getMessage()));
		}
		return user;
	}

}
