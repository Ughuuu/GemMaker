package com.gem.component;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.engine.EngineInfo;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

import lombok.Getter;
import lombok.val;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.jsync.sync.Sync;

/**
 * @author Dragos
 * @composed 1 - 1 Script
 */
public class ComponentScript extends ComponentBase {
	private static final String scriptFolder = ".";
	private static final String outputFolder = "data";
	protected Sync<Script> script;
	private static final String javaVersion = "-1.8";
	@Getter
	private String scriptName;

	public ComponentScript(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
		this.enable = false;
	}

	public Script getScript() {
		return script.getInstance();
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

	public boolean isValid() {
		if (script != null && script.getInstance() != null && script.getInstance().gem != null
				&& script.getInstance().holder != null) {
			return true;
		}
		return false;
	}

	@Override
	public ComponentScript setEnabled(boolean Enable) {
		this.enable = Enable;
		return this;
	}

	public void runInit() throws Exception{
		if (script != null && script.getInstance() != null) {
			// TODO maybe keek this : if (script.getChanged()) {
				script.getInstance().gem = gem;
				script.getInstance().holder = getOwner();
				script.getInstance().onInit();
			//}
		}
	}

	public String update() throws Exception {
		String err = loadClass(scriptName);
		if (err.equals("")) {
			runInit();
		}
		return err;
	}

	public String update(Set<ComponentScript> scripts) throws Exception {
		List list = scripts.stream().map(t -> t.script).collect(Collectors.toList());
		if (list == null)
			return update();
		String err = loadClasses(scriptName, list);
		return err;
	}

	public void reloadOthers(Set<ComponentScript> scripts) throws Exception {
		List list = scripts.stream().map(t -> t.script).collect(Collectors.toList());
		if (list == null)
			return;
		script.reloadClasses(list);
	}

	public boolean shouldUpdate() {
		return script == null || script.needsChange();
	}

	public ComponentScript setScript(Class<?> name) {
		setScript(name.getCanonicalName());
		return this;
	}

	private String loadClass(String name) throws Exception {
		scriptName = name;
		String err = "";
		if (script == null) {
			script = new Sync<Script>(name, scriptFolder, outputFolder).setOptions(javaVersion);
			err = script.update();
		} else {
			script.setClassName(name);
			err = script.update();
		}
		return err;
	}

	private String loadClasses(String name, List<Sync> scripts) throws Exception {
		scriptName = name;
		String err = "";
		if (script == null) {
			script = new Sync<Script>(name, scriptFolder, outputFolder).setOptions(javaVersion);
			err = script.update(scripts);
		} else {
			script.setClassName(name);
			err = script.update(scripts);
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
			if (EngineInfo.Debug) {
				script = new Sync<Script>(scriptName, scriptFolder, outputFolder).setOptions(javaVersion);
				// val err = loadClass(scriptName);
				// if (err != null && err.length() != 0) {
				// System.out.println(err);
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Debugger.log(e.toString());
		}
		enable = false;
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", type.getName()).element("Script").attribute("Name", scriptName)
				.pop().pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
