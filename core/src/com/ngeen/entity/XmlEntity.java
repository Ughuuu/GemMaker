package com.ngeen.entity;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentScript;
import com.ngeen.component.XmlComponent;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;

public class XmlEntity {
	private final XmlComponent _XmlComponent;
	private final Ngeen _Ng;
	private final String path = "scenes/";

	public XmlEntity(Ngeen _Ng, XmlComponent _XmlComponent) {
		this._Ng = _Ng;
		this._XmlComponent = _XmlComponent;
	}

	public void Save() {
		try {
			String scene = _Ng.getCurrentScene().getName();
			FileHandle handle = Gdx.files.local(path + scene + ".xml");
			handle.writeString(Dump(), false);
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
			Debugger.log(e.toString());
			//e.printStackTrace();
		}
		return "";
	}

	public void Load() {
		try {
			String scene = _Ng.getCurrentScene().getName();
			XmlReader xml = new XmlReader();
			XmlReader.Element element = xml.parse(Gdx.files.local(path + scene + ".xml"));
			_Ng.EntityBuilder.clear();
			EngineInfo.Width = element.getFloat("Width");
			EngineInfo.Height = element.getFloat("Height");
			for (Element el : element.getChildrenByName("Entity")) {
				String name = el.get("Name");
				_Ng.EntityBuilder.makeEntity(name).Load(el, _XmlComponent);
			}
			List<Entity> entities = _Ng.EntityBuilder.getEntities();
			for (Entity ent : entities) {
				ent.setParent(_Ng.getEntity(ent._ParentName));
				if(ent.hasComponent(ComponentScript.class)){
					ent.getComponent(ComponentScript.class).setEnabled(true);
				}
			}
			_Ng.resize((int)EngineInfo.ScreenWidth, (int)EngineInfo.ScreenHeight);
		} catch (Exception e) {
			Debugger.log(e.toString());
			//e.printStackTrace();
		}
	}
}
