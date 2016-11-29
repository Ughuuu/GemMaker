package com.gem.entity;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.jsync.sync.Commiter;
import org.jsync.sync.Updater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.asset.Asset;
import com.gem.asset.AssetFactory;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentScript;
import com.gem.component.XmlComponent;
import com.gem.engine.EngineInfo;
import com.gem.engine.Gem;

import lombok.val;

/**
 * @author Dragos
 * @hidden
 */
public class XmlEntity {
	protected final XmlComponent xmlComponent;
	private final Gem gem;
	private final String path = "data/scenes/";
	private final StringWriter stringWriter;
	private final XmlReader xmlReader;
	private final XmlWriter xmlWriter;
	private boolean loading = false;
	private final Updater updater;

	public XmlEntity(Gem _Ng) {
		this.gem = _Ng;
		xmlReader = new XmlReader();
		stringWriter = new StringWriter();
		xmlWriter = new XmlWriter(stringWriter);
		xmlComponent = new XmlComponent(xmlReader, xmlWriter, stringWriter, this);
		Updater updaterCopy = null;
		try {
			updaterCopy = new Commiter("data", "master");
		} catch (Exception e) {
			e.printStackTrace();
		}
		updater = updaterCopy;
	}

	public <T extends ComponentBase> ComponentBase callAddUnsafe(Entity ent, Class<T> cls) {
		return ent.addComponent(cls);
	}

	public String Dump() {
		try {
			loading = true;
			stringWriter.getBuffer().setLength(0);
			String scene = gem.getCurrentScene().getName();
			xmlWriter.element(scene).attribute("Width", EngineInfo.Width).attribute("Height", EngineInfo.Height);

			List<Entity> entities = gem.entityBuilder.getEntities();
			for (Entity ent : entities) {
				SaveEntity(ent);
			}
			xmlWriter.pop();
			return stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		loading = false;
		return "";
	}

	public void Load() {
		try {
			String scene = gem.getCurrentScene().getName();
			scene = scene.replace('.', '/');
			XmlReader.Element element;
			if (EngineInfo.Debug && EngineInfo.Applet == false && EngineInfo.Android == false) {
				element = xmlReader.parse(Gdx.files.local(path + scene + ".xml"));
			} else {
				FileHandle f = Gdx.files.internal(AssetFactory.prePath + path + scene + ".xml");
				System.out.println(f.file().getAbsolutePath());
				element = xmlReader.parse(f);
			}
			EngineInfo.Width = element.getFloat("Width");
			EngineInfo.Height = element.getFloat("Height");
			gem.restart();
			for (Element el : element.getChildrenByName("Entity")) {
				LoadEntity(el);
			}
			List<Entity> entities = gem.entityBuilder.getEntities();
			for (Entity ent : entities) {
				ent.setParent(ent._ParentName);
				if (ent.hasComponent(ComponentScript.class)) {
					ent.getComponent(ComponentScript.class).setEnabled(true);
				}
			}
			gem.resize((int) EngineInfo.ScreenWidth, (int) EngineInfo.ScreenHeight);
		} catch (Exception e) {
			// Debugger.log(e.toString());
			e.printStackTrace();
		}
	}

	public Entity LoadEntity(String s) throws Exception {
		val el = xmlReader.parse(s);
		val name = el.get("Name");
		int nr = 0;
		while (gem.entityBuilder.getByName(name + nr) != null) {
			nr++;
		}
		val ent = gem.entityBuilder.makeEntity(name + nr);
		return ent;
	}

	public void LoadEntity(XmlReader.Element el) throws Exception {
		String name = el.get("Name");
		Entity ent = gem.entityBuilder.makeEntity(name);
		ent.Load(el, xmlComponent);
		val components = ent.getComponents();
		for(val comp : components){
			ent.componentFactory.notifyAllComponents(ent.getComponents(), comp);
		}
	}

	public void Save() {
		try {
			String scene = gem.getCurrentScene().getName();
			scene = scene.replace('.', '/');
			FileHandle handle = Gdx.files.local(path + scene + ".xml");
			handle.writeString(Dump(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String SaveEntity(Entity ent) throws Exception {
		if (loading == false) {
			stringWriter.getBuffer().setLength(0);
		}
		ent.Save(xmlWriter, xmlComponent);
		return stringWriter.toString();
	}
	
	private void handleDelete(String path){
		
	}
	
	private void handleAdd(String path){
		
	}

	public void update() {
		if (EngineInfo.Android == true || EngineInfo.Applet == true) {
			return;
		}
		List<DiffEntry> diffs = null;
		try {
			diffs = updater.update();
		} catch (GitAPIException | IOException e1) {
			e1.printStackTrace();
			return;
		}
		if(diffs.size() == 0)
			return;
		Asset asset = null;
		int resId = -1;
		for (DiffEntry diff : diffs) {
			switch (diff.getChangeType()) {
			case ADD:
				handleAdd(diff.getNewPath());
				break;
			case COPY:
			case RENAME:
				handleDelete(diff.getOldPath());
				handleAdd(diff.getNewPath());
				break;
			case MODIFY:
				handleDelete(diff.getOldPath());
				handleAdd(diff.getNewPath());
				break;
			case DELETE:
				handleDelete(diff.getOldPath());
				break;
			}
		}
		gem.loader.finish();
		// TODO maybe reput this?
		//try {
			//String scene = gem.getCurrentScene().getName();
			//scene = scene.replace('.', '/');
			//Load();
		//} catch (Exception e) {
			//e.printStackTrace();
		//}
	}
}
