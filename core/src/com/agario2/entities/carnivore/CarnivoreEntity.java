package com.agario2.entities.carnivore;

import com.agario2.Game;
import com.agario2.agents.AgentType;
import com.agario2.agents.manager.EntityManager;
import com.agario2.entities.Renderable;
import com.agario2.entities.Updatable;
import com.agario2.info.AgentInfo;
import com.agario2.util.Vector2Utils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CarnivoreEntity implements Renderable, Updatable {

	private final EntityManager entityManager;
	private final Map<String, AgentInfo> agentsInfoMap;

	private String localName;
	private Vector2 position;
	private final float speed;
	private int radius;
	private final int maxRadius;

	public CarnivoreEntity(final EntityManager entityManager, String localName, Vector2 position) {
		this.entityManager = entityManager;
		this.agentsInfoMap = new ConcurrentHashMap<>();

		this.localName = localName;
		this.position = position;
		this.speed = 0.5f;
		this.radius = 15;
		this.maxRadius = 150;
	}

	@Override
	public void render(Game game) {
		game.renderEntity(localName, position, Color.RED, radius);
	}

	@Override
	public void update() {
		AgentInfo closestAgent = null;
		float minDistance = Float.MAX_VALUE;

		for (Map.Entry<String, AgentInfo> entry : agentsInfoMap.entrySet()) {
			String agentLocalName = entry.getKey();
			AgentInfo agent = entry.getValue();

			float distance = position.dst(agent.getPosition());
			float radiusSum = radius + agent.getRadius();

			boolean isSmallerOrBiggerCarnivore = agent.getRadius() != radius && agent.getType() == AgentType.CARNIVORE;
			boolean isSmallerCarnivore = agent.getRadius() < radius && agent.getType() == AgentType.CARNIVORE;
			boolean isValidHerbivore = agent.getRadius() / 2 < radius && agent.getType() == AgentType.HERBIVORE;

			if (distance < minDistance && (isSmallerOrBiggerCarnivore || isValidHerbivore)) {
				minDistance = distance;
				closestAgent = agent;
			}

			if (distance <= radiusSum && (isSmallerCarnivore || isValidHerbivore)) {
				entityManager.deleteAgent(agentLocalName);
				agentsInfoMap.remove(agentLocalName);

				radius = Math.min(radius + agent.getRadius(), maxRadius);
			}
		}

		if (closestAgent != null) {
			Vector2 direction = closestAgent.getPosition().cpy().sub(position).nor();

			if (closestAgent.getRadius() > radius && closestAgent.getType() == AgentType.CARNIVORE) {
				direction.scl(-1);
			}

			Vector2 velocity = direction.scl(speed);
			position.add(velocity);
		}

		position = Vector2Utils.clampCircle(position, radius);
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void putAgentsInfoMap(String key, AgentInfo value) {
		this.agentsInfoMap.put(key, value);
	}

	public void removeAgentsInfoMap(String key) {
		this.agentsInfoMap.remove(key);
	}
}
