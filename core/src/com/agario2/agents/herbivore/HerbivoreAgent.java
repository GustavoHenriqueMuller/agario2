package com.agario2.agents.herbivore;

import com.agario2.agents.AbstractAgent;
import com.agario2.agents.AgentType;
import com.agario2.entities.herbivore.HerbivoreEntity;
import com.agario2.info.AgentInfo;

import java.util.Arrays;
import java.util.List;

public class HerbivoreAgent extends AbstractAgent {

	private HerbivoreEntity entity;

	@Override
	public void setup() {
		entity = (HerbivoreEntity) getArguments()[0];
		super.setup();
	}

	@Override
	public AgentInfo getMessagePayload() {
		AgentInfo info = new AgentInfo();

		info.setPosition(entity.getPosition());
		info.setType(AgentType.HERBIVORE);
		info.setRadius(entity.getRadius());

		return info;
	}

	@Override
	public List<AgentType> getReceivers() {
		return Arrays.asList(AgentType.CARNIVORE);
	}

	@Override
	public void putAgentsInfoMap(String key, AgentInfo value) {
		entity.putAgentsInfoMap(key, value);
	}

	@Override
	public void removeAgentsInfoMap(String key) {
		entity.removeAgentsInfoMap(key);
	}
}
