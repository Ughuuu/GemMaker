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

/**
 * @author Dragos
 * @composed 1 - 1 Script
 */
public class ComponentScript extends ComponentBase {
    protected Sync<Script> Program;
    private String _ProgramName;

    public ComponentScript(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        Program = null;
    }

    public Script getScript() {
        return Program;
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

    public boolean isValid() {
        if (Program != null && Program.ng != null && Program.holder != null) {
            return true;
        }
        return false;
    }

    @Override
    public ComponentScript setEnabled(boolean Enable) {
        this.Enable = Enable;
        if (Program != null)
            Program.onInit();
        return this;
    }

    private void makeProxyScript(String name) throws Exception {
        Sync syncCode = new Sync();
        syncCode.loadFromFile(Paths.get("assets/scripts/" + name.replace('.', '/') + ".java"), "scripts." + name);
        //Program = syncCode.get();

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

    @Override
    protected void destroyed() {
        if(Program==null)
            return;
        Program.ng = null;
        Program.holder = null;
        Program = null;
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        try {
            _ProgramName = element.getChildByName("Program").get("Name");
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
                if (tryTwice) {
                    Class<?> act = Class.forName(_ProgramName);
                    makeScript(act);
                }
            } catch (Exception e) {
                Debugger.log(e.toString());
                // e.printStackTrace();
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
