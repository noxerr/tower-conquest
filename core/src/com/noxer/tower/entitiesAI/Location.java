package com.noxer.tower.entitiesAI;

import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.math.Vector2;

public class Location extends SteerableAdapter<Vector2>{
	private Vector2 vector;
	
	public Location(float x, float y) {
		vector = new Vector2(x, y);
	}
	
	public Location(Vector2 vector2) {
		vector = vector2;
	}
	
	@Override
	public Vector2 getPosition() {
		return vector;
	}

	@Override
	public float getOrientation() {
		return 0;
	}

}
