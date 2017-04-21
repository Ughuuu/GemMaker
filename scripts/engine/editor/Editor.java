package scripts.engine.editor;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.gem.component.ComponentCamera;
import com.gem.component.ComponentPoint;
import com.gem.component.Script;
import com.gem.entity.Entity;

public class Editor extends Script {
	private ComponentPoint point;
	private Vector3 pos1 = new Vector3(), pos2 = new Vector3();
	private Vector3 viewport = new Vector3();
	private Vector3 screen = new Vector3();
	private Vector3 scale = new Vector3();
	private Draw draw;

	private boolean click = false; 
	private int modifyType = 0;
	public Set<Entity> selected = new HashSet<Entity>();

	@Override
	public void onInit() {
    	point = holder.getComponent(ComponentPoint.class);
		draw = holder.getScript(Draw.class);
	}

	void computeSelected() {
		selected.clear();
		for (Entity ent : gem.entityBuilder.getEntities()) {
			if(ent.getName().indexOf('~') != -1){
				continue;
			}
			Vector3 pos = point.getPosition();
			Vector3 otherPos = ent.getComponent(ComponentPoint.class).getPosition();
			otherPos.sub(pos);
			float lenx = Math.abs(pos1.x - pos2.x);
			float leny = Math.abs(pos1.y - pos2.y);

			float len2x = Math.abs(otherPos.x - pos2.x) + Math.abs(otherPos.x - pos1.x);
			float len2y = Math.abs(otherPos.y - pos2.y) + Math.abs(otherPos.y - pos1.y);
			if (len2x - lenx <= 1 && len2y - leny <= 1) {
				selected.add(ent);
			}
		}
	} 

	void drawEntities() {
		for (Entity ent : gem.entityBuilder.getEntities()) {
			if(ent.getName().indexOf('~') != -1){
				continue;
			}
			Vector3 pos = point.getPosition();
			Vector3 otherPos = ent.getComponent(ComponentPoint.class).getPosition();
			otherPos.sub(pos);
			draw.drawX(otherPos, 5);
			if (ent.hasChildren()) {
				for (Entity child : ent.getChildren()) {
					Vector3 parentPos = child.getComponent(ComponentPoint.class).getPosition();
					parentPos.sub(pos);
					draw.drawLine(otherPos.x, otherPos.y, parentPos.x, parentPos.y, Color.WHITE);
					draw.drawRectangle(parentPos.cpy().sub(2, -10, 0), parentPos.cpy().add(2, 6, 0), Color.GREEN);
				}
			}
		}
	}

	void drawSelection() {
		ComponentCamera parentCamera = holder.getLastParent().getComponent(ComponentCamera.class);
		viewport.set(parentCamera.getViewportWidth(), parentCamera.getViewportHeight(), 0);
		screen.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
		scale.set(viewport).scl(1 / screen.x, 1 / screen.y, 1);

		if (Gdx.input.isTouched() && click == false) {
			click = true;
			pos1.set(Gdx.input.getX(), -Gdx.input.getY(), 0).scl(scale).add(-viewport.x / 2, viewport.y / 2, 0);
		} else if (!Gdx.input.isTouched()) {
			click = false;
			pos1.set(0, 0, 0);
			pos2.set(0, 0, 0);
		}
		if (Gdx.input.isTouched()) {
			pos2.set(Gdx.input.getX(), -Gdx.input.getY(), 0).scl(scale).add(-viewport.x / 2, viewport.y / 2, 0);
		}
		draw.drawRectangle(pos1, pos2, Color.WHITE);
	}

	void drawSelected() {
		for (Entity ent : selected) {
			Vector3 pos = point.getPosition();
			Vector3 otherPos = ent.getComponent(ComponentPoint.class).getPosition();
			otherPos.sub(pos);
			draw.drawRectangle(otherPos.sub(5), otherPos.cpy().add(10), Color.BLACK);
		}
	}

	private void moveAll() {
		for (Entity ent : selected) {
			if (ent.getParent() != null && selected.contains(ent.getParent()))
				continue;
			float deltaX = Gdx.input.getDeltaX(), deltaY = Gdx.input.getDeltaY();
			Vector3 modif = new Vector3(deltaX, deltaY, 0);
			modif.y *= -1;
			switch (modifyType) {
			case 1:
				Vector3 pos = new Vector3(ent.getComponent(ComponentPoint.class).getRelativePosition());
				pos.add(modif);
				ent.getComponent(ComponentPoint.class).setRelativePosition(pos);
				break;
			case 2:
				Vector3 rot = new Vector3(ent.getComponent(ComponentPoint.class).getRelativeRotation());
				rot.add(0, 0, (deltaX - deltaY) / 2);
				ent.getComponent(ComponentPoint.class).setRelativeRotation(rot);
				System.out.println(ent.getComponent(ComponentPoint.class).getRotation());
				break;
			case 3:
				Vector3 sc = new Vector3(ent.getComponent(ComponentPoint.class).getRelativeScale());
				modif.y *= -1;
				sc.add(modif.scl(1 / 100.0f));
				ent.getComponent(ComponentPoint.class).setRelativeScale(sc);
				break;
			}
		}
	}

	void getModificationType() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			if (modifyType != 0)
				modifyType = 0;
			else
				modifyType = 3;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			if (modifyType != 0)
				modifyType = 0;
			else
				modifyType = 2;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
			if (modifyType != 0)
				modifyType = 0;
			else
				modifyType = 1;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			gem.changeScene(gem.getCurrentScene().getName());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			point.setRelativePosition(point.getPosition().sub(5, 0, 0));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			point.setRelativePosition(point.getPosition().add(5, 0, 0));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			point.setRelativePosition(point.getPosition().add(0, 5, 0));
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			point.setRelativePosition(point.getPosition().sub(0, 5, 0));
		}
		if(modifyType == 0){
			Gdx.input.setCursorCatched(false);
		}else{
			Gdx.input.setCursorCatched(true);			
		}
	}

	@Override
	public void onUpdate(float delta) {
		drawEntities();
		drawSelection();
		if (Gdx.input.isTouched()) {
			modifyType = 0;
			computeSelected();
		}
		getModificationType();
		moveAll();
		drawSelected();
	}
}