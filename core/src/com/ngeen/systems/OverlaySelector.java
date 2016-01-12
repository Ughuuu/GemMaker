package com.ngeen.systems;

import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.ComponentSprite;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class OverlaySelector {
	final SystemOverlay _SystemOverlay;
	private ShapeRenderer _ShapeRenderer;
	private final Ngeen _Ng;

	public OverlaySelector(SystemOverlay overlay, ShapeRenderer _ShapeRenderer, Ngeen ng) {
		_SystemOverlay = overlay;
		this._ShapeRenderer = _ShapeRenderer;
		_Ng = ng;
	}

	boolean selectPoint(Vector3 point, BoundingBox _Selection) {
		Entity ent2 = _Ng.EntityBuilder.getByName("~CAMERA");
		ComponentCamera cam2 = ent2.getComponent(ComponentCamera.class);
		Vector3 proj = new Vector3(point).mul(cam2.Camera.view);
		if (_Selection.contains(proj)) {
			return true;
		}
		return false;
	}

	boolean selectPoint(Entity ent, BoundingBox _Selection) {
		return selectPoint(ent.getComponent(ComponentPoint.class).getPosition(), _Selection);
	}

	boolean selectPointSprite(Entity ent, BoundingBox _Selection) {
		Vector3 point = ent.getComponent(ComponentPoint.class).getPosition();
		Sprite spr = ent.getComponent(ComponentSprite.class).getSprite();
		Vector3 p1 = new Vector3(point), p2 = new Vector3(point), p3 = new Vector3(point), p4 = new Vector3(point);
		float w = spr.getWidth() / 2 * spr.getScaleX();
		float h = spr.getHeight() / 2 * spr.getScaleY();
		p1.x += w;
		p1.y += h;
		p2.x -= w;
		p2.y -= h;
		p3.x += w;
		p3.y -= w;
		p4.x -= h;
		p4.y += w;
		if (selectPoint(p1, _Selection) && selectPoint(p2, _Selection) && selectPoint(p3, _Selection)
				&& selectPoint(p4 , _Selection)) {
			return true;
		}
		return false;
	}

	protected void Select(Entity ent, Set<Entity> _Selected, BoundingBox _Selection) {
		ComponentPoint point = ent.getComponent(ComponentPoint.class);
		Matrix4 pos = point.getMatrix();
		if (ent.hasComponent(ComponentSprite.class)) {
			if (selectPointSprite(ent, _Selection)) {
				_Selected.add(ent);
			}
		} else {
			if (selectPoint(ent, _Selection)) {
				_Selected.add(ent);
			}
		}
	}

	boolean onCircle(Vector3 mouse, Vector3 point, float radius) {
		Entity ent2 = _Ng.EntityBuilder.getByName("~CAMERA");
		ComponentCamera cam2 = ent2.getComponent(ComponentCamera.class);
		Vector3 proj = new Vector3(point).mul(cam2.Camera.view);
		if (proj.sub(mouse).len()<radius){
			return true;
		}
		return false;
	}
	
	protected boolean OnEntity(Vector3 mouse, Entity ent) {
		ComponentPoint point = ent.getComponent(ComponentPoint.class);
		if (ent.hasComponent(ComponentSprite.class)) {
			ComponentSprite spriteComp = ent.getComponent(ComponentSprite.class);
			Sprite sprite = spriteComp.getSprite();
			float w = sprite.getWidth() / 2 * sprite.getScaleX();
			float h = sprite.getHeight() / 2 * sprite.getScaleY();
			if (onCircle(mouse, point.getPosition(), Float.max(w, h))) {
				return true;
			}
		} else {
			if (onCircle(mouse, point.getPosition(), 12)) {
				return true;
			}
		}
		return false;
	}

	protected void Overlay(Entity ent, Matrix4 _Comb, boolean _Selected) {
		ComponentPoint point = ent.getComponent(ComponentPoint.class);
		Matrix4 pos = point.getMatrix();
		_ShapeRenderer.setProjectionMatrix(new Matrix4(_Comb).mul(pos));
		if (ent.hasComponent(ComponentSprite.class)) {
			ComponentSprite spriteComp = ent.getComponent(ComponentSprite.class);
			Sprite sprite = spriteComp.getSprite();
			float w = sprite.getWidth() / 2 * sprite.getScaleX();
			float h = sprite.getHeight() / 2 * sprite.getScaleY();
			if (_Selected) {
				_ShapeRenderer.circle(0, 0, Float.max(w, h));
			}
		} else {
			if (_Selected)
				_ShapeRenderer.circle(0, 0, 6);
			_ShapeRenderer.line(-3, -3, 3, 3);
			_ShapeRenderer.line(-3, 3, 3, -3);
			_ShapeRenderer.line(-3, -3, 3, 3);
			_ShapeRenderer.line(-3, 3, 3, -3);
		}
	}
}
