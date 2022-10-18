package com.agario2.agents.manager;

import com.agario2.agents.carnivore.CarnivoreAgent;
import com.agario2.agents.food.FoodAgent;
import com.agario2.agents.herbivore.HerbivoreAgent;
import com.agario2.entities.Renderable;
import com.agario2.entities.Updatable;
import com.agario2.entities.carnivore.CarnivoreEntity;
import com.agario2.entities.food.FoodEntity;
import com.agario2.entities.herbivore.HerbivoreEntity;
import com.agario2.util.ClassUtil;
import com.agario2.util.Constants;
import com.agario2.util.Vector2Utils;
import com.badlogic.gdx.utils.Disposable;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManager implements Disposable {

	private final AgentContainer container;
	private final Map<String, AgentController> controllerNames = new ConcurrentHashMap<>();;

	private final Map<String, CarnivoreEntity> carnivoreEntities = new ConcurrentHashMap<>();
	private final Map<String, HerbivoreEntity> herbivoreEntities = new ConcurrentHashMap<>();
	private final Map<String, FoodEntity> foodEntities = new ConcurrentHashMap<>();

	private int carnivoreCount = 0;
	private int herbivoreCount = 0;
	private int foodCount = 0;

	public EntityManager() {
		Properties properties = new ExtendedProperties();
		properties.setProperty(Profile.GUI, "true");
		properties.setProperty(Profile.MAIN, "false");

		this.container = Runtime.instance().createMainContainer(new ProfileImpl(properties));

		try {
			container.start();
		} catch (ControllerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void dispose() {
		try {
			this.container.kill();
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}
	}

	public void spawnInitialAgents() throws StaleProxyException {
		for (int i = 0; i < Constants.STARTING_CARNIVORE_AMOUNT; i++)
			spawnCarnivore();

		for (int i = 0; i < Constants.STARTING_HERBIVORE_AMOUNT; i++)
			spawnHerbivore();

		for (int i = 0; i < Constants.STARTING_FOOD_AMOUNT; i++)
			spawnFood();
	}

	public void spawnCarnivore() throws StaleProxyException {
		String agentLocalName = Constants.CARNIVORE_PREFIX + carnivoreCount;
		CarnivoreEntity data = new CarnivoreEntity(this, agentLocalName, Vector2Utils.getRandomPosition());

		AgentController controller = this.container.createNewAgent(
			agentLocalName,
			ClassUtil.getClassPath(CarnivoreAgent.class),
			new Object[] {data, this}
		);

		controller.start();

		controllerNames.put(agentLocalName, controller);
		carnivoreEntities.put(agentLocalName, data);
		carnivoreCount++;
	}

	public void spawnHerbivore() throws StaleProxyException {
		String agentLocalName = Constants.HERBIVORE_PREFIX + herbivoreCount;
		HerbivoreEntity data = new HerbivoreEntity(this, agentLocalName, Vector2Utils.getRandomPosition());

		AgentController controller = this.container.createNewAgent(
			agentLocalName,
			ClassUtil.getClassPath(HerbivoreAgent.class),
			new Object[] {data, this}
		);

		controller.start();

		controllerNames.put(agentLocalName, controller);
		herbivoreEntities.put(agentLocalName, data);
		herbivoreCount++;
	}

	public void spawnFood() throws StaleProxyException {
		String agentLocalName = Constants.FOOD_PREFIX + foodCount;
		FoodEntity data = new FoodEntity(agentLocalName, Vector2Utils.getRandomPosition());

		AgentController controller = this.container.createNewAgent(
			agentLocalName,
			ClassUtil.getClassPath(FoodAgent.class),
			new Object[] {data, this}
		);

		controller.start();

		controllerNames.put(agentLocalName, controller);
		foodEntities.put(agentLocalName, data);
		foodCount++;
	}

	public void deleteAgent(String agentLocalName) {
		AgentController controller = controllerNames.get(agentLocalName);

		if (controller == null) {
			return;
		}

		try {
			controller.kill();
			controllerNames.remove(agentLocalName);
		}
		catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}

		if (agentLocalName.startsWith(Constants.CARNIVORE_PREFIX)) {
			carnivoreEntities.remove(agentLocalName);
		} else if (agentLocalName.startsWith(Constants.HERBIVORE_PREFIX)) {
			herbivoreEntities.remove(agentLocalName);
		} else if (agentLocalName.startsWith(Constants.FOOD_PREFIX)) {
			foodEntities.remove(agentLocalName);
		}
	}

	public boolean hasAgent(String localName) {
		return controllerNames.get(localName) != null;
	}

	public List<Updatable> getUpdatables() {
		List<Updatable> updatables = new ArrayList<Updatable>(carnivoreEntities.values());

		updatables.addAll(herbivoreEntities.values());
		updatables.addAll(foodEntities.values());

		return updatables;
	}

	public List<Renderable> getRenderables() {
		List<Renderable> renderables = new ArrayList<Renderable>(carnivoreEntities.values());

		renderables.addAll(herbivoreEntities.values());
		renderables.addAll(foodEntities.values());

		return renderables;
	}

	public Set<String> getCarnivoreLocalNames() {
		return carnivoreEntities.keySet();
	}

	public Set<String> getHerbivoreLocalNames() {
		return herbivoreEntities.keySet();
	}

	public Set<String> getFoodLocalNames() {
		return foodEntities.keySet();
	}

	public int getCarnivoreAmount() {
		return carnivoreEntities.size();
	}

	public int getHerbivoreAmount() {
		return herbivoreEntities.size();
	}

	public int getFoodAmount() {
		return foodEntities.size();
	}
}
