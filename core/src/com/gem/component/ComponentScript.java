package com.gem.component;

import org.jsync.sync.Sync;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.engine.EngineInfo;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

import lombok.val;

/**
 * @author Dragos
 * @composed 1 - 1 Script
 */
public class ComponentScript extends ComponentBase {
	private static final String scriptFolder = ".";
	private static final String outputFolder = "data";
	protected Sync<Script> script;
	private String scriptName;

	public ComponentScript(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
	}

	public Script getScript() {
		return script.getInstance();
	}

	public boolean isValid() {
		if (script != null && script.getInstance() != null && script.getInstance().gem != null
				&& script.getInstance().holder != null) {
			return true;
		}
		return false;
	}

	@Override
	public ComponentScript setEnabled(boolean Enable) {
		this.Enable = Enable;
		if (script != null && script.getInstance() != null)
			script.getInstance().onInit();
		return this;
	}
	
	public String update() throws Exception{
		return loadClass(scriptName);
	}

	public ComponentScript setScript(Class<?> name) {
		setScript(name.getCanonicalName());
		return this;
	}

	public ComponentScript setScript(String name) {
		try {
			if (EngineInfo.Debug) {
				loadClass(name);
				script.getInstance().onInit();
				return this;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	private String loadClass(String name) throws Exception {
		scriptName = name;
		String err = "";
		if (script == null) {
			script = new Sync<Script>(name, scriptFolder, outputFolder).setOptions("-1.7");
			err = script.update();
		} else {
			err = script.update(name);
		}
		if(script.getInstance() == null)
			return err;
		script.getInstance().gem = gem;
		script.getInstance().holder = getOwner();
		if(script.getChanged()){
			script.getInstance().onInit();
		}
		return err;
	}

	@Override
	protected void destroyed() {
		if (script == null)
			return;
		script = null;
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		try {
			scriptName = element.getChildByName("Script").get("Name");
			try {
				if (EngineInfo.Debug) {
					loadClass(scriptName);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// Debugger.log(e.toString());
			}
			Enable = false;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			// Debugger.log(e.toString());
		}
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", Type.getName()).element("Script")
				.attribute("Name", scriptName).pop().pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
