package com.agario2.agents.carnivore;

import com.agario2.agents.AbstractAgent;
import com.agario2.agents.AgentType;
import com.agario2.entities.carnivore.CarnivoreEntity;
import com.agario2.info.AgentInfo;

import java.util.Arrays;
import java.util.List;

public class CarnivoreAgent extends AbstractAgent {

	private CarnivoreEntity entity;

	@Override
	protected void setup() {
		entity = (CarnivoreEntity) getArguments()[0];
		super.setup();
	}

	@Override
	public AgentInfo getMessagePayload() {
		AgentInfo info = new AgentInfo();

		info.setPosition(entity.getPosition());
		info.setType(AgentType.CARNIVORE);
		info.setRadius(entity.getRadius());

		return info;
	}

	@Override
	public List<AgentType> getReceivers() {
		return Arrays.asList(AgentType.CARNIVORE, AgentType.HERBIVORE);
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

