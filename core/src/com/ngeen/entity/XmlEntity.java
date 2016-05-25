package com.ngeen.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.asset.AssetFactory;
import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentScript;
import com.ngeen.component.XmlComponent;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

/**
 * @author Dragos
 * @hidden
 */
public class XmlEntity {
    protected final XmlComponent _XmlComponent;
    private final Ngeen _Ng;
    private final String path = "scenes/";
    private final StringWriter stringWriter;
    private final XmlReader xmlReader;
    private final XmlWriter xmlWriter;
    private int _SaveTime;
    private boolean loading = false;

    public XmlEntity(Ngeen _Ng) {
        this._Ng = _Ng;
        xmlReader = new XmlReader();
        stringWriter = new StringWriter();
        xmlWriter = new XmlWriter(stringWriter);
        _XmlComponent = new XmlComponent(xmlReader, xmlWriter, stringWriter, this);
    }

    public <T extends ComponentBase> ComponentBase callAddUnsafe(Entity ent, Class<T> cls) {
        return ent.addComponent(cls);
    }

    public void checkDate() {
        if (EngineInfo.Android == true || EngineInfo.Applet == true) {
            return;
        }
        try {
            String scene = _Ng.getCurrentScene().getName();
            File f = Gdx.files.local(path + scene + ".xml").file();
            BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);

            FileTime ft = attr.lastModifiedTime();
            int time2 = (int) (ft.toMillis() * 1000);

            if (time2 != _SaveTime) {
                Load();
            }
            _SaveTime = time2;
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public String Dump() {
        try {
            loading = true;
            stringWriter.getBuffer().setLength(0);
            String scene = _Ng.getCurrentScene().getName();
            xmlWriter.element(scene).attribute("Width", EngineInfo.Width).attribute("Height", EngineInfo.Height);

            List<Entity> entities = _Ng.EntityBuilder.getEntities();
            for (Entity ent : entities) {
                SaveEntity(ent);
            }
            xmlWriter.pop();
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            // e.printStackTrace();
        }
        loading = false;
        return "";
    }

    public void Load() {
        try {
            String scene = _Ng.getCurrentScene().getName();
            scene = scene.replace('.', '/');
            XmlReader.Element element;
            if (EngineInfo.Debug && EngineInfo.Applet == false && EngineInfo.Android == false) {
                element = xmlReader.parse(Gdx.files.local(path + scene + ".xml"));
            } else {
                FileHandle f = Gdx.files.internal(AssetFactory._PrePath + path + scene + ".xml");
                System.out.println(f.file().getAbsolutePath());
                element = xmlReader.parse(f);
            }
            EngineInfo.Width = element.getFloat("Width");
            EngineInfo.Height = element.getFloat("Height");
            _Ng.restart();
            for (Element el : element.getChildrenByName("Entity")) {
                LoadEntity(el);
            }
            List<Entity> entities = _Ng.EntityBuilder.getEntities();
            for (Entity ent : entities) {
                ent.setParent(ent._ParentName);
                if (ent.hasComponent(ComponentScript.class)) {
                    ent.getComponent(ComponentScript.class).setEnabled(true);
                }
            }
            _Ng.resize((int) EngineInfo.ScreenWidth, (int) EngineInfo.ScreenHeight);
        } catch (Exception e) {
            Debugger.log(e.toString());
            // e.printStackTrace();
        }
    }

    public Entity LoadEntity(String s) throws Exception {
        XmlReader.Element el = xmlReader.parse(s);
        String name = el.get("Name");
        while (_Ng.EntityBuilder.getByName(name) != null) {
            name += "1";
        }
        Entity ent = _Ng.EntityBuilder.makeEntity(name);
        ent.Load(el, _XmlComponent);
        return ent;
    }

    public void LoadEntity(XmlReader.Element el) throws Exception {
        String name = el.get("Name");
        _Ng.EntityBuilder.makeEntity(name).Load(el, _XmlComponent);
    }

    public void Save() {
        try {
            String scene = _Ng.getCurrentScene().getName();
            scene = scene.replace('.', '/');
            FileHandle handle = Gdx.files.local(path + scene + ".xml");
            _SaveTime = (int) (TimeUtils.millis() / 1000);
            handle.writeString(Dump(), false);

            File f = Gdx.files.local(path + scene + ".xml").file();
            BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);

            FileTime ft = attr.lastModifiedTime();
            _SaveTime = (int) (ft.toMillis() * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String SaveEntity(Entity ent) throws Exception {
        if (loading == false) {
            stringWriter.getBuffer().setLength(0);
        }
        ent.Save(xmlWriter, _XmlComponent);
        return stringWriter.toString();
    }
}
