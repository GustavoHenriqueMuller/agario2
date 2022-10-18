package com.agario2.agents;

import com.agario2.agents.manager.EntityManager;
import com.agario2.info.AgentInfo;
import com.agario2.util.Constants;
import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;
import java.util.Set;

public abstract class AbstractAgent extends Agent {

	private EntityManager entityManager;

	@Override
	protected void setup() {
		entityManager = (EntityManager) getArguments()[1];

		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage receivedMessage = receive();

				if (receivedMessage != null) {
					String ontology = receivedMessage.getOntology();

					if (ontology.equals("destroy")) {
						removeAgentsInfoMap(receivedMessage.getSender().getLocalName());
					} else if (ontology.equals("position")) {
						AgentInfo payload = deserializeMessagePayload(receivedMessage.getContent());
						String senderLocalName = receivedMessage.getSender().getLocalName();

						if (entityManager.hasAgent(senderLocalName)) {
							putAgentsInfoMap(senderLocalName, payload);
						}
					}
				}
			}
		});

		addBehaviour(new TickerBehaviour(this, 20) {
			@Override
			public void onTick() {
				ACLMessage positionMessage = new ACLMessage(ACLMessage.INFORM);

				positionMessage.setContent(serializeMessagePayload(getMessagePayload()));
				positionMessage.setOntology("position");

				sendMessage(positionMessage);
			}
		});
	}

	@Override
	protected void takeDown() {
		ACLMessage destroyMessage = new ACLMessage(ACLMessage.INFORM);

		destroyMessage.setOntology("destroy");
		destroyMessage.setContent("");

		sendMessage(destroyMessage);
	}

	private void sendMessage(ACLMessage message) {
		if (getReceivers().contains(AgentType.CARNIVORE)) {
			sendMessageToEntities(message, Constants.CARNIVORE_PREFIX, entityManager.getCarnivoreLocalNames());
		}

		if (getReceivers().contains(AgentType.HERBIVORE)) {
			sendMessageToEntities(message, Constants.HERBIVORE_PREFIX, entityManager.getHerbivoreLocalNames());
		}

		if (getReceivers().contains(AgentType.FOOD)) {
			sendMessageToEntities(message, Constants.FOOD_PREFIX, entityManager.getFoodLocalNames());
		}
	}

	private void sendMessageToEntities(ACLMessage message, String prefix, Set<String> receiverLocalNames) {
		for (String name : receiverLocalNames) {
			if (!name.equals(getLocalName())) {
				message.addReceiver(new AID(name, AID.ISLOCALNAME));
			}
		}

		send(message);
	}

	private String serializeMessagePayload(AgentInfo payload) {
		return new Gson().toJson(payload);
	}

	private AgentInfo deserializeMessagePayload(String payload) {
		return new Gson().fromJson(payload, AgentInfo.class);
	}

	public abstract AgentInfo getMessagePayload();
	public abstract List<AgentType> getReceivers();

	public abstract void putAgentsInfoMap(String key, AgentInfo value);
	public abstract void removeAgentsInfoMap(String key);
}

