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
	protected Sync<Script> program;
	private String programName;

	public ComponentScript(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
	}

	public Script getScript() {
		return program.getInstance();
	}

	public boolean isValid() {
		if (program != null && program.getInstance() != null && program.getInstance().gem != null
				&& program.getInstance().holder != null) {
			return true;
		}
		return false;
	}

	@Override
	public ComponentScript setEnabled(boolean Enable) {
		this.Enable = Enable;
		if (program != null && program.getInstance() != null)
			program.getInstance().onInit();
		return this;
	}
	
	public String update() throws Exception{
		return loadClass(programName);
	}

	public ComponentScript setScript(Class<?> name) {
		setScript(name.getCanonicalName());
		return this;
	}

	public ComponentScript setScript(String name) {
		try {
			if (EngineInfo.Debug) {
				loadClass(name);
				program.getInstance().onInit();
				return this;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Debugger.log(e.getStackTrace());
		}
		return this;
	}

	private String loadClass(String name) throws Exception {
		programName = name;
		String err = "";
		if (program == null) {
			program = new Sync<Script>(name, scriptFolder, outputFolder).setOptions("-1.7");
			err = program.update();
		} else {
			err = program.update(name);
		}
		if(program.getInstance() == null)
			return err;
		program.getInstance().gem = Ng;
		program.getInstance().holder = getOwner();
		if(program.getChanged()){
			program.getInstance().onInit();
		}
		return err;
	}

	@Override
	protected void destroyed() {
		if (program == null)
			return;
		program = null;
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		try {
			programName = element.getChildByName("Program").get("Name");
			try {
				if (EngineInfo.Debug) {
					loadClass(programName);
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
		element.element("Component").attribute("Type", Type.getName()).element("Program")
				.attribute("Name", programName).pop().pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
