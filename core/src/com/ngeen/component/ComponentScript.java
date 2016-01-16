package com.ngeen.component;

import java.io.File;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

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
			if(EngineInfo.Debug){
				makeProxyScript(name);
			}else{
				Class<?> act = Class.forName(name);
				makeScript(act);
			}
			Program.onInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public ComponentScript setScript(Class<?> name){
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
	
	public boolean isValid(){
		if(Program!= null && Program.ng != null && Program.holder != null){
			return true;			
		}
		return false;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).attribute("_ProgramName", _ProgramName).pop();
	}

	public ComponentScript setEnabled(boolean Enable) {
		this.Enable = Enable;
		Program.onInit();
		return this;
	}

	@Override
	protected void Load(Element element) throws Exception {
		try {
			_ProgramName = element.get("_ProgramName");
			if(EngineInfo.Debug){
				makeProxyScript(_ProgramName);
			}else{
				Class<?> act = Class.forName(_ProgramName);
				makeScript(act);
			}
			Enable = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
