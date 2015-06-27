package com.ngeen.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent extends Component {
	public Vector2 position;
	public float angle;
	public Vector2 scale;
	public int z;

	public TransformComponent() {
		position = new Vector2(0, 0);
		angle = 0;
		scale = new Vector2(0, 0);
		z = 0;
	}
}