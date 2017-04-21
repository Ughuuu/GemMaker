package scripts.engine.editor;

import com.badlogic.gdx.math.Vector3;
import com.gem.component.ComponentCamera;
import com.gem.component.ComponentPoint;
import com.gem.component.Script;
import com.gem.component.ui.ComponentUILabel;
import com.gem.entity.Entity;

public class Properties extends Script {
	private ComponentUILabel label;
	private ComponentPoint point;
	private ComponentPoint parent;
	private ComponentCamera camera;
	private Editor editor;

	@Override
	public void onInit() {
		label = holder.getComponent(ComponentUILabel.class);
		point = holder.getComponent(ComponentPoint.class);
		parent = holder.getParent().getComponent(ComponentPoint.class);
		editor = gem.getEntity("~EDITOR").getScript(Editor.class);
		camera = holder.getLastParent().getComponent(ComponentCamera.class);
	}

	int once = 0;
	
	@Override
	public void onUpdate(float delta) {
		label.setText("");
		if (editor.selected != null && editor.selected.isEmpty())
			return;
		Entity ent = editor.selected.iterator().next();
		String data = gem.entityBuilder.saveEntity(ent);
		
		Vector3 parentPos = parent.getPosition();
		
		point.setRelativePosition(new Vector3(-parentPos.x + camera.getViewportWidth() - 100
				,-parentPos.y + 100, 0));

		label.setText(data);
	}
}