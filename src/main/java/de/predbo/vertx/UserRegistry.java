package de.predbo.vertx;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import de.predbo.vertx.model.User;

public class UserRegistry {

	private static final Logger _logger = LoggerFactory.getLogger(SampleVerticle.class);
	private static final AtomicInteger USER_IDS = new AtomicInteger(0);
	
	private final Map<Integer, User> _registry = new LinkedHashMap<>();

	protected void createSampleRegistry() {
		_registry.clear();
		USER_IDS.set(0);
		registerUser(new User("Hugo", "Müller"));
		registerUser(new User("Berta", "Schmidt"));
		registerUser(new User("Max", "Kruse"));
	}

	public Map<Integer, User> getUsers() {
		return _registry;
	}
	
	public User getUser(Integer id) {
		return _registry.get(id);
	}

	public void registerUser(User user) {
		user.setId(USER_IDS.incrementAndGet());
		_registry.put(user.getId(), user);
	}
	
	public void unregisterUser(Integer id) {
		_registry.remove(id);
	}
	
	public void updateUser(Integer id, User user) {
		_registry.put(id, user);
	}
	
	public User getUserByUnvalidatedId(String idString)  {
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
