package com.ngeen.entity;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.XmlComponent;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;

public class XmlEntity {
	private final XmlComponent _XmlComponent;
	private final Ngeen _Ng;

	public XmlEntity(Ngeen _Ng, XmlComponent _XmlComponent) {
		this._Ng = _Ng;
		this._XmlComponent = _XmlComponent;
	}

	public void Save() {
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
			FileHandle handle = Gdx.files.external(scene + ".xml");
			handle.writeString(writer.toString(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Dump() {
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
			FileHandle handle = Gdx.files.external(scene + ".xml");
			Debugger.log(writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Load() {
		try {
			String scene = _Ng.getCurrentScene().getName();
			XmlReader xml = new XmlReader();
			XmlReader.Element element = xml.parse(Gdx.files.external(scene + ".xml"));
			_Ng.EntityBuilder.clear();
			EngineInfo.Width = element.getFloat("Width");
			EngineInfo.Height = element.getFloat("Height");
			for (Element el : element.getChildrenByName("Entity")) {
				String name = el.get("Name");
				_Ng.EntityBuilder.makeEntity(name).Load(el, _XmlComponent);
			}
			Dump();
		} catch (Exception e) {
			//Debugger.log(e.toString());
			e.printStackTrace();
		}
	}
}
