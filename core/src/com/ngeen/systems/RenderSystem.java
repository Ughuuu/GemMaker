package com.ngeen.systems;

import com.ngeen.entity.Entity;

public class RenderSystem extends SystemBase {
	// private SpriteBatch batch;

	@SuppressWarnings("unchecked")
	public RenderSystem() {
		super(new SystemConfiguration().all());
		// this.batch = new SpriteBatch();
	}

	private void changeCamera() {
		// Matrix4 comb =
		// Constant.CAMERA.getComponent(CameraComponent.class).camera.combined;
		// Matrix4 pos =
		// ComputeHelper.getCombined(Constant.CAMERA.getComponent(TransformComponent.class));
		// batch.setProjectionMatrix(pos.mul(comb));
	}

	@Override
	public void onUpdate(Entity ent) {
		/*
		 * TransformComponent transform = transformMapper.get(e);
		 * TextureComponent texture = textureMapper.get(e);
		 * 
		 * if (texture.draw == true && texture.tex != null) { Sprite tex =
		 * texture.tex; if (Math.abs(transform.scale.x - tex.getScaleX()) >
		 * Constant.EPSILON || Math.abs(transform.scale.y - tex.getScaleY()) >
		 * Constant.EPSILON) { tex.setScale(transform.scale.x,
		 * transform.scale.y); } if (Math.abs(transform.angle -
		 * tex.getRotation()) > Constant.EPSILON) {
		 * tex.setRotation(transform.angle); } if (Math.abs(transform.position.x
		 * - texture.tex.getWidth() * 0.5f - tex.getX()) > Constant.EPSILON ||
		 * Math .abs(transform.position.y - texture.tex.getHeight() * 0.5f -
		 * tex.getY()) < Constant.EPSILON) {
		 * tex.setPosition(transform.position.x - texture.tex.getWidth() * 0.5f,
		 * transform.position.y - texture.tex.getHeight() * 0.5f); }
		 * texture.tex.draw(batch); }
		 */
	}

}