package com.agario2;

import com.agario2.agents.manager.EntityManager;
import com.agario2.entities.Renderable;
import com.agario2.entities.Updatable;
import com.agario2.util.Constants;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import jade.wrapper.StaleProxyException;

public class Game extends ApplicationAdapter {

	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private BitmapFont bitmapFont;
	private EntityManager entityManager;

	private float carnivoreTimer = 0;
	private float herbivoreTimer = 0;
	private float foodTimer = 0;

	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		bitmapFont = new BitmapFont();
		entityManager = new EntityManager();

		try {
			entityManager.spawnInitialAgents();
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}

		Gdx.graphics.setWindowedMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 1, 1, 1);
		Gdx.graphics.setTitle(Constants.WINDOW_TITLE + " v " + Constants.VERSION + " - " + Gdx.graphics.getFramesPerSecond() + "fps");

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.rect(
			0,
			0,
			Gdx.graphics.getWidth(),
			Gdx.graphics.getHeight(),
			new Color(0x37383bff),
			new Color(0x37383bff),
			new Color(0x464646ff),
			new Color(0x464646ff)
		);

		shapeRenderer.end();

		for (Renderable renderable : entityManager.getRenderables()) {
			renderable.render(this);
		}

		update();
	}

	private void update() {
		for (Updatable updatable : entityManager.getUpdatables()) {
			updatable.update();
		}

		updateTimers();
	}

	private void updateTimers() {
		carnivoreTimer += Gdx.graphics.getDeltaTime();
		herbivoreTimer += Gdx.graphics.getDeltaTime();
		foodTimer += Gdx.graphics.getDeltaTime();

		try {
			if (carnivoreTimer >= Constants.CARNIVORE_TIMER) {
				entityManager.spawnCarnivore();
				carnivoreTimer = 0;
			}

			if (herbivoreTimer >= Constants.HERBIVORE_TIMER) {
				entityManager.spawnHerbivore();
				herbivoreTimer = 0;
			}

			if (foodTimer >= Constants.FOOD_TIMER) {
				entityManager.spawnFood();
				foodTimer = 0;
			}
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		spriteBatch.dispose();
		bitmapFont.dispose();
	}

	public void renderEntity(String localName, Vector2 position, Color color, int radius) {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		{
			shapeRenderer.setColor(color);
			shapeRenderer.circle(position.x, position.y, radius);
		}
		shapeRenderer.end();

		spriteBatch.begin();
		{
			GlyphLayout layout = new GlyphLayout(bitmapFont, localName);

			float textX = position.x - layout.width / 2;
			float textY = position.y + radius + layout.height + 5;

			bitmapFont.draw(spriteBatch, localName, textX, textY);
		}
		spriteBatch.end();
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public BitmapFont getBitmapFont() {
		return bitmapFont;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}
