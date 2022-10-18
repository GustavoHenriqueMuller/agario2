package com.agario2.entities.food;

import com.agario2.Game;
import com.agario2.entities.Renderable;
import com.agario2.entities.Updatable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class FoodEntity implements Renderable, Updatable {

	private String localName;
	private Vector2 position;
	private int radius;

	public FoodEntity(String localName, Vector2 position) {
		this.localName = localName;
		this.position = position;
		this.radius = 5;
	}

	@Override
	public void render(Game game) {
		game.renderEntity(localName, position, Color.PURPLE, radius);
	}

	@Override
	public void update() {}

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
}
