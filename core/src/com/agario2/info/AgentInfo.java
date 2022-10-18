package com.agario2.info;

import com.agario2.agents.AgentType;
import com.badlogic.gdx.math.Vector2;

public class AgentInfo {

	private Vector2 position;
	private AgentType type;
	private int radius;

	public AgentInfo() {}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public AgentType getType() {
		return type;
	}

	public void setType(AgentType type) {
		this.type = type;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
}

