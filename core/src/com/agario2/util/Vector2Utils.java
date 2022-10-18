package com.agario2.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Vector2Utils {

	public static Vector2 getRandomPosition() {
		int randomX = MathUtils.random(0, Gdx.graphics.getWidth());
		int randomY = MathUtils.random(0, Gdx.graphics.getHeight());

		return new Vector2(randomX, randomY);
	}

	public static Vector2 clampCircle(Vector2 position, float size) {
		float x = Math.max(size, Math.min(position.x, Gdx.graphics.getWidth() - size));
		float y = Math.max(size, Math.min(position.y, Gdx.graphics.getHeight() - size));

		return new Vector2(x, y);
	}
}
