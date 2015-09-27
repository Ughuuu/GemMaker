package com.ngeen.helper;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.ngeen.components.TransformComponent;

public class ComputeHelper {
	public static Matrix4 getCombined(TransformComponent tr) {
		Matrix4 comb = new Matrix4(new Vector3(tr.position.x, tr.position.y, 0),
				new Quaternion(new Vector3(0, 0, 1), tr.angle), new Vector3(tr.scale.x, tr.scale.y, 1));
		return comb;
	}
}
