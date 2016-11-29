package com.gem.systems;

import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gem.action.CommandFactory;
import com.gem.component.ComponentCamera;
import com.gem.component.ComponentPoint;
import com.gem.engine.EngineInfo;
import com.gem.engine.Gem;
import com.gem.entity.Entity;
import com.gem.entity.XmlEntity;

/**
 * @author Dragos
 * @composed 1 - 1 OverlaySelector
 */
public class SystemOverlay extends SystemBase implements GestureListener, InputProcessor {
	public static float x;
	public static float y;
	public static float x1;
	public static float y1;
	public static float x2;
	public static float y2;
	private final XmlEntity xmlFactory;
	private Set<Entity> allEntities = new TreeSet<Entity>(new Comparator<Entity>() {

		@Override
		public int compare(Entity o1, Entity o2) {
			return Integer.signum(o1.hashCode() - o2.hashCode());
		}
	});
	private boolean deleteSelected = false, selectAll = false, writeMode = false, shift = false, ctrl, alt;
	private String entityName = "null";
	private int modifyType = 0;
	private OverlaySelector overlaySelector;
	public static Set<Entity> selected = new TreeSet<Entity>(new Comparator<Entity>() {

		@Override
		public int compare(Entity o1, Entity o2) {
			return Integer.signum(o1.hashCode() - o2.hashCode());
		}
	});
	private boolean selecting = false;
	private BoundingBox selection = new BoundingBox();
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private boolean reload;

	public SystemOverlay(Gem ng, SystemConfiguration conf, SpriteBatch batch, XmlEntity xml) {
		super(ng, conf);
		xmlFactory = xml;
		shapeRenderer = new ShapeRenderer();
		overlaySelector = new OverlaySelector(this, shapeRenderer, gem);
		spriteBatch = batch;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPrintableChar(char c) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		return (!Character.isISOControl(c)) && c != KeyEvent.CHAR_UNDEFINED && block != null
				&& block != Character.UnicodeBlock.SPECIALS;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (Input.Keys.SHIFT_LEFT == keycode || Input.Keys.SHIFT_RIGHT == keycode) {
			shift = true;
		}
		if (Input.Keys.CONTROL_LEFT == keycode || Input.Keys.CONTROL_RIGHT == keycode) {
			ctrl = true;
		}
		if (Input.Keys.ALT_LEFT == keycode || Input.Keys.ALT_RIGHT == keycode) {
			alt = true;
		}
		if (Input.Keys.SPACE == keycode) {
			xmlFactory.Save();
		}
		if (Input.Keys.Z == keycode && ctrl) {
			CommandFactory.factory.undoAction();
		}
		if (Input.Keys.Y == keycode && ctrl) {
			CommandFactory.factory.redoAction();
		}
		if (Input.Keys.L == keycode && shift) {
			reload = true;
		}
		if (Input.Keys.BACKSPACE == keycode) {
			if (entityName.length() != 0)
				entityName = entityName.substring(0, entityName.length() - 1);
			selected.iterator().next().setName(entityName);
		}
		if (Input.Keys.FORWARD_DEL == keycode) {
			deleteSelected = true;
		}
		if (Input.Keys.F2 == keycode) {
			if (selected.size() == 0) {
				return false;
			}
			if (writeMode) {
				writeMode = false;
			} else {
				writeMode = true;
				entityName = selected.iterator().next().getName();
			}
		}
		if (Input.Keys.ENTER == keycode) {
			writeMode = false;
		}
		if (writeMode) {
			return false;
		}
		if (Input.Keys.C == keycode) {
			selected.clear();
			Entity ent = gem.entityBuilder.makeEntity("NewEntity");
			ent.addComponent(ComponentPoint.class);
			selected.add(ent);
		}
		if (Input.Keys.A == keycode) {
			selectAll = true;
		}
		if (Input.Keys.S == keycode) {
			if (modifyType != 0)
				modifyType = 0;
			else
				modifyType = 3;
		}
		if (Input.Keys.R == keycode) {
			if (modifyType != 0)
				modifyType = 0;
			else
				modifyType = 2;
		}
		if (Input.Keys.T == keycode) {
			if (modifyType != 0)
				modifyType = 0;
			else
				modifyType = 1;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (writeMode) {
			if (isPrintableChar(character)) {
				entityName += character;
				selected.iterator().next().setName(entityName);
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (Input.Keys.SHIFT_LEFT == keycode || Input.Keys.SHIFT_RIGHT == keycode) {
			shift = false;
		}
		if (Input.Keys.CONTROL_LEFT == keycode || Input.Keys.CONTROL_RIGHT == keycode) {
			ctrl = false;
		}
		if (Input.Keys.ALT_LEFT == keycode || Input.Keys.ALT_RIGHT == keycode) {
			alt = false;
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		x = screenX;
		y = screenY;
		return false;
	}

	@Override
	public void onAfterUpdate() {
		shapeRenderer.end();

		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(gem.entityBuilder.getByName("~EDITOR").getComponent(ComponentCamera.class).getCombined());
		BitmapFont font = (BitmapFont) gem.loader.getAsset("engine/fonts/impact.fnt").getAsset();
		font.getData().setScale(0.2f);
		for (Entity ent : allEntities) {
			Matrix4 onlyPos = new Matrix4(ent.getComponent(ComponentPoint.class).getPosition(), new Quaternion(),
					new Vector3(1, 1, 1));
			spriteBatch.setTransformMatrix(onlyPos);
			font.draw(spriteBatch, ent.getName(), 0, 0);
		}
		spriteBatch.end();
		allEntities.clear();

		if (reload) {
			resetState();
			xmlFactory.Load();
		}
	}

	@Override
	public void onBeforeUpdate() {
		x%=EngineInfo.ScreenWidth;
		y%=EngineInfo.ScreenHeight;
		x1%=EngineInfo.ScreenWidth;
		y1%=EngineInfo.ScreenHeight;
		x2%=EngineInfo.ScreenWidth;
		y2%=EngineInfo.ScreenHeight;
		if(x<0)x+=EngineInfo.ScreenWidth;
		if(x1<0)x1+=EngineInfo.ScreenWidth;
		if(x2<0)x2+=EngineInfo.ScreenWidth;
		if(y<0)y+=EngineInfo.ScreenHeight;
		if(y1<0)y1+=EngineInfo.ScreenHeight;
		if(y2<0)y2+=EngineInfo.ScreenHeight;
		
		drawMouse();
		
		if (selectAll) {
			SelectAll();
		}
		if (deleteSelected) {
			DeleteAll();
		}
		shapeRenderer.begin(ShapeType.Line);
		computeSelection();
		if (modifyType != 0) {
			MoveAll();
			return;
		}
		if (selecting && !shift)
			selected.clear();
		drawSelection();
	}

	@Override
	public void onUpdate(Entity ent) {
		allEntities.add(ent);
		if (selecting)
			overlaySelector.Select(ent, selected, selection);

		overlaySelector.Overlay(ent, gem.entityBuilder.getByName("~EDITOR").getComponent(ComponentCamera.class).getCombined(), selected.contains(ent));
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pinchStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean scrolled(int amount) {
		Entity camera = gem.entityBuilder.getByName("~EDITOR");
		ComponentCamera cam = camera.getComponent(ComponentCamera.class);
		ComponentPoint pos = camera.getComponent(ComponentPoint.class);
		float ratio = cam.getViewportWidth() / cam.getViewportHeight();
		pos.setScale(pos.getScale().add(ratio * amount / 80));
		float posx = (EngineInfo.ScreenWidth / 2 - x) / EngineInfo.ScreenWidth * amount * 80 * ratio;
		float posy = (EngineInfo.ScreenHeight / 2 - y) / EngineInfo.ScreenHeight * amount * 80 * ratio * -1;
		pos.setPosition(pos.getPosition().add(new Vector3(posx, posy, 0)));
		EngineInfo.Width = cam.getViewportWidth();
		EngineInfo.Height = cam.getViewportHeight();
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		SystemOverlay.x = x;
		SystemOverlay.y = y;
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == 2){
			Gdx.input.setCursorCatched(false);
		}else{
			Gdx.input.setCursorCatched(true);
		}
		writeMode = false;
		modifyType = 0;
		if (button != Input.Buttons.LEFT) {
			return false;
		}
		selecting = true;
		x1 = screenX;
		y1 = screenY;
		x2 = x1;
		y2 = y1;
		x = screenX;
		y = screenY;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		selecting = true;
		x2 = screenX;
		y2 = screenY;
		x = screenX;
		y = screenY;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT) {
			return false;
		}
		selecting = false;
		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;
		x = screenX;
		y = screenY;
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	private void computeSelection() {
		Entity ent = gem.entityBuilder.getByName("~UICAMERA");
		ComponentCamera cam = ent.getComponent(ComponentCamera.class);

		selection = new BoundingBox(new Vector3(x1, EngineInfo.ScreenHeight - y1, -10),
				new Vector3(x2, EngineInfo.ScreenHeight - y2, 10));
		Vector3 position = cam.getPosition();
		selection.mul(new Matrix4().translate(new Vector3(-position.x, -position.y, 0)));
		selection
				.mul(new Matrix4(new Vector3(), new Quaternion(), new Vector3(EngineInfo.Width / EngineInfo.ScreenWidth,
						EngineInfo.Height / EngineInfo.ScreenHeight, 1)));
	}
	
	private void drawMouse(){
		Entity ent = gem.entityBuilder.getByName("~UICAMERA");
		ComponentCamera cam = ent.getComponent(ComponentCamera.class);
		Matrix4 comb = cam.getCombined();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rect(-EngineInfo.ScreenWidth/2, -EngineInfo.ScreenHeight/2, EngineInfo.ScreenWidth, EngineInfo.ScreenHeight);
		shapeRenderer.setProjectionMatrix(comb);
		shapeRenderer.rect(0, 0, EngineInfo.ScreenWidth, EngineInfo.ScreenHeight);
		shapeRenderer.line(x+16, EngineInfo.ScreenHeight-y-12, 
				x, EngineInfo.ScreenHeight-y);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.triangle(x + 2, EngineInfo.ScreenHeight-y - 6, 
				x + 10, EngineInfo.ScreenHeight-y, 
				x, EngineInfo.ScreenHeight-y);
		shapeRenderer.end();
	}

	private void drawSelection() {
		Entity ent = gem.entityBuilder.getByName("~UICAMERA");
		ComponentCamera cam = ent.getComponent(ComponentCamera.class);
		Matrix4 comb = cam.getCombined();
		shapeRenderer.setProjectionMatrix(comb);
		shapeRenderer.rect(x1, EngineInfo.ScreenHeight - y1, x2 - x1, -y2 + y1);
	}

	private void MoveAll() {
		for (Entity ent : selected) {
			if (ent.getParent() != null && selected.contains(ent.getParent()))
				continue;
			float deltaX = Gdx.input.getDeltaX(), deltaY = Gdx.input.getDeltaY();
			Vector3 modif = new Vector3(deltaX, deltaY, 0);
			modif.mul(new Matrix4(new Vector3(), new Quaternion(), new Vector3(
					EngineInfo.Width / EngineInfo.ScreenWidth, EngineInfo.Height / EngineInfo.ScreenHeight, 1)));
			modif.y *= -1;
			switch (modifyType) {
			case 1:
				Vector3 pos = new Vector3(ent.getComponent(ComponentPoint.class).getPosition());
				pos.add(modif);
				ent.getComponent(ComponentPoint.class).setPosition(pos);
				break;
			case 2:
				Vector3 rot = new Vector3(ent.getComponent(ComponentPoint.class).getRotation());
				rot.add(0, 0, (deltaX - deltaY)/1000);
				ent.getComponent(ComponentPoint.class).setRotation(rot);
				break;
			case 3:
				Vector3 sc = new Vector3(ent.getComponent(ComponentPoint.class).getScale());
				modif.y *= -1;
				sc.add(modif.scl(1 / 100.0f));
				ent.getComponent(ComponentPoint.class).setScale(sc);
				break;
			}
		}
		x1 = x2;
		y1 = y2;
	}

	void DeleteAll() {
		for (Entity ent : selected) {
			ent.remove();
		}
		selected.clear();
		deleteSelected = false;
	}

	void resetState() {
		reload = false;
		modifyType = 0;
		deleteSelected = false;
		selectAll = false;
		writeMode = false;
		entityName = "null";
	}

	void SelectAll() {
		selected.clear();
		for (Entity ent : gem.entityBuilder.getEntities()) {
			if (ent.hasComponent(ComponentPoint.class) && !ent.hasComponent(ComponentCamera.class))
				selected.add(ent);
		}
		selectAll = false;
	}
}
