package com.ngeen.component;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.asset.Asset;
import com.ngeen.engine.Ngeen;

public class ComponentVariable extends ComponentBase {
	private Map<String, Object> _DataMap;
	public boolean Update = false;
	
	public ComponentVariable(Ngeen ng) {
		super(ng);
		_DataMap = new HashMap<String, Object>();
	}
	
	public void setData(String name, Object var){
		_DataMap.put(name, var);
		Update = true;
	}
	
	public Object getData(String name){
		return _DataMap.get(name);
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component")
		.attribute("_Type", "ComponentVariable");
		for(Map.Entry<String, Object> obj : _DataMap.entrySet()){
			String name = obj.getKey();
			Asset tex = (Asset) obj.getValue();
			element.element("Attribute").
			attribute("AttributeName", name).
			attribute("AttributeLocation", tex.getFolder() + tex.getPath()).
			pop();
		}
		element.pop();
	}

	@Override
	protected void Load(Element element) throws Exception {		
		for (Element target : element.getChildrenByName("Attribute")) {
			String AttributeName = target.get("AttributeName");
			String AttributeLocation = target.get("AttributeLocation");
			setData(AttributeName, _Ng.Loader.getAsset(AttributeLocation));
		}
		Update = true;
	}
}
