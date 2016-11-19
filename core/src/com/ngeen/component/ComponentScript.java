package com.ngeen.component;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

import java.io.File;
import java.nio.file.Paths;

import org.jsync.sync.Commiter;
import org.jsync.sync.Sync;
import org.jsync.sync.Updater;

/**
 * @author Dragos
 * @composed 1 - 1 Script
 */
public class ComponentScript extends ComponentBase {
	protected static final Updater updater;
    protected Sync<Script> Program;
    private String _ProgramName;
    
    static{
    	Updater updaterCpy = null;
    	try{
    		updaterCpy = new Commiter();
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    	updater = updaterCpy;
    }

    public ComponentScript(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
    }

    public Script getScript() {
        return Program.getInstance();
    }

    public ComponentScript setScript(Class<?> name) {
    	setScript(name.getCanonicalName());
        return this;
    }

    public ComponentScript setScript(String name) {
        try {
            if (EngineInfo.Debug) {
                loadClass(name);
                Program.getInstance().onInit();
                return this;
            }
        } catch (Exception e) {
            Debugger.log(e.getStackTrace());
        }
        return this;
    }

    public boolean isValid() {
        if (Program != null && Program.getInstance() != null && Program.getInstance().ng != null && Program.getInstance().holder != null) {
            return true;
        }
        return false;
    }

    @Override
    public ComponentScript setEnabled(boolean Enable) {
        this.Enable = Enable;
        if (Program != null)
            Program.getInstance().onInit();
        return this;
    }

    private void loadClass(String name) throws Exception {        
        _ProgramName = name;
        if(Program == null){
        	Program = new Sync<Script>(name, updater);
        }
        else{
        	Program.update(name);
        }
        Program.getInstance().ng = _Ng;
        Program.getInstance().holder = getOwner();
    }

    @Override
    protected void destroyed() {
        if(Program==null)
            return;
        Program = null;
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        try {
            _ProgramName = element.getChildByName("Program").get("Name");
            try {
                if (EngineInfo.Debug) {
                    loadClass(_ProgramName);
                }
            } catch (Exception e) {
                Debugger.log(e.toString());
            }
            Enable = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Debugger.log(e.toString());
        }
        return this;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        element.element("Component").attribute("Type", _Type.getName()).element("Program")
                .attribute("Name", _ProgramName).pop().pop();
    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        component.notifyWithComponent(this);
    }
}
