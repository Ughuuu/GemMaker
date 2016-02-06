package com.ngeen.component;

import java.io.File;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

/** 
 * @composed 1 - 1 Script
 * @author Dragos
 *
 */
public class ComponentScript extends ComponentBase {
	protected Script Program;
	private String _ProgramName;

	public ComponentScript(Ngeen ng, Entity ent) {
		super(ng, ent);
		Program = null;
	}

	private void makeProxyScript(String name) throws Exception {
		DynaCode dynacode = new DynaCode(_Ng, getOwner());
		dynacode.addSourceDir(new File("scripts"));
		Program = (Script) dynacode.newClassInstance(Script.class, name);

		_ProgramName = name;
		Program.ng = _Ng;
		Program.holder = getOwner();
	}

	private void makeScript(Class<?> script) throws Exception {
		_ProgramName = script.getName();
		Program = (Script) script.newInstance();
		Program.ng = _Ng;
		Program.holder = getOwner();
	}

	public ComponentScript setScript(String name) {
		try {
			if (EngineInfo.Debug) {
				makeProxyScript(name);
				Program.onInit();
				return this;
			}
		} catch (Exception e) {
			Debugger.log(e.getStackTrace());
		}
		try {
			Class<?> act = Class.forName(name);
			makeScript(act);
			Program.onInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public ComponentScript setScript(Class<?> name) {
		try {
			makeScript(name);
			Program.onInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public Script getScript() {
		return Program;
	}

	public boolean isValid() {
		if (Program != null && Program.ng != null && Program.holder != null) {
			return true;
		}
		return false;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).element("_ProgramName")
				.attribute("String", _ProgramName).pop().pop();
	}

	public ComponentScript setEnabled(boolean Enable) {
		this.Enable = Enable;
		if(Program != null)
		Program.onInit();
		return this;
	}

	@Override
	protected void Load(Element element) throws Exception {
		try {
			_ProgramName = element.getChildByName("_ProgramName").get("String");
			boolean tryTwice = true;
			try {
				if (EngineInfo.Debug) {
					makeProxyScript(_ProgramName);
					tryTwice = false;
				}
			} catch (Exception e) {
				Debugger.log(e.toString());
			}
			try {
				if(tryTwice){
				Class<?> act = Class.forName(_ProgramName);
				makeScript(act);
				}
			} catch (Exception e) {
				Debugger.log(e.toString());
				//e.printStackTrace();
			}
			Enable = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Debugger.log(e.toString());
		}
	}
}
