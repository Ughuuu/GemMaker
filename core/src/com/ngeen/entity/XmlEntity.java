package com.ngeen.entity;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.asset.AssetFactory;
import com.ngeen.component.ComponentScript;
import com.ngeen.component.XmlComponent;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;

/**
 * @hidden
 * @author Dragos
 *
 */
public class XmlEntity {
	private final XmlComponent _XmlComponent;
	private final Ngeen _Ng;
	private final String path = "scenes/";
	private int _SaveTime;

	public XmlEntity(Ngeen _Ng, XmlComponent _XmlComponent) {
		this._Ng = _Ng;
		this._XmlComponent = _XmlComponent;
	}

	public void Save() {
		try {
			String scene = _Ng.getCurrentScene().getName();
			scene = scene.replace('.', '/');
			FileHandle handle = Gdx.files.local(path + scene + ".xml");
			_SaveTime = (int) (TimeUtils.millis() / 1000);
			handle.writeString(Dump(), false);

			File f = Gdx.files.local(path + scene + ".xml").file();
			BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);

			FileTime ft = attr.lastModifiedTime();
			_SaveTime = (int) (ft.toMillis()*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String Dump() {
		try {
			String scene = _Ng.getCurrentScene().getName();
			StringWriter writer = new StringWriter();
			XmlWriter xml = new XmlWriter(writer);
			xml.element(scene).attribute("Width", EngineInfo.Width).attribute("Height", EngineInfo.Height);

			List<Entity> entities = _Ng.EntityBuilder.getEntities();
			for (Entity ent : entities) {
				ent.Save(xml, _XmlComponent);
			}
			xml.pop();
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			// e.printStackTrace();
		}
		return "";
	}

	public void Load() {
		try {
			String scene = _Ng.getCurrentScene().getName();
			scene = scene.replace('.', '/');
			XmlReader xml = new XmlReader();
			XmlReader.Element element ;
			if(EngineInfo.Debug && EngineInfo.Applet == false){
				element = xml.parse(Gdx.files.local(path + scene + ".xml"));
			}else{
				FileHandle f = Gdx.files.internal(AssetFactory._PrePath + path + scene + ".xml");
				System.out.println(f.file().getAbsolutePath());
				element = xml.parse(f);
			}
			EngineInfo.Width = element.getFloat("Width");
			EngineInfo.Height = element.getFloat("Height");
			_Ng.restart();
			for (Element el : element.getChildrenByName("Entity")) {
				String name = el.get("Name");
				if(!name.equals("~CAMERA") && !name.equals("~UICAMERA"))
				_Ng.EntityBuilder.makeEntity(name).Load(el, _XmlComponent);
			}
			List<Entity> entities = _Ng.EntityBuilder.getEntities();
			for (Entity ent : entities) {
				ent.setParent(ent._ParentName);
				if (ent.hasComponent(ComponentScript.class)) {
					ent.getComponent(ComponentScript.class).setEnabled(true);
				}
			}
			_Ng.resize((int) EngineInfo.ScreenWidth, (int) EngineInfo.ScreenHeight);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkDate() {
		try {
		String scene = _Ng.getCurrentScene().getName();
		File f = Gdx.files.local(path + scene + ".xml").file();
		BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);

		FileTime ft = attr.lastModifiedTime();
		int time2 = (int) (ft.toMillis()*1000);
		
			if (time2 != _SaveTime) {
				Load();
			}
			_SaveTime = time2;
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
