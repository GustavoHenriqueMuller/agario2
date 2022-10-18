package com.agario2.agents.food;

import com.agario2.agents.AbstractAgent;
import com.agario2.agents.AgentType;
import com.agario2.entities.food.FoodEntity;
import com.agario2.info.AgentInfo;

import java.util.Arrays;
import java.util.List;

public class FoodAgent extends AbstractAgent {

	private FoodEntity entity;

	@Override
	protected void setup() {
		entity = (FoodEntity) getArguments()[0];
		super.setup();
	}

	@Override
	public AgentInfo getMessagePayload() {
		AgentInfo info = new AgentInfo();

		info.setPosition(entity.getPosition());
		info.setType(AgentType.FOOD);
		info.setRadius(entity.getRadius());

		return info;
	}

	@Override
	public List<AgentType> getReceivers() {
		return Arrays.asList(AgentType.HERBIVORE);
	}

	@Override
	public void putAgentsInfoMap(String key, AgentInfo value) {}

	@Override
	public void removeAgentsInfoMap(String key) {}
}
