package com.gem.component;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.entity.Entity;
import com.gem.entity.XmlEntity;

import java.io.StringWriter;

/**
 * @author Dragos
 * @hidden
 */
public class XmlComponent {
    final StringWriter stringWriter;
    final XmlEntity xmlEntity;
    final XmlReader xmlReader;
    final XmlWriter xmlWriter;

    public XmlComponent(XmlReader xmlReader, XmlWriter xmlWriter, StringWriter stringWriter, XmlEntity xmlEntity) {
        this.xmlReader = xmlReader;
        this.xmlWriter = xmlWriter;
        this.xmlEntity = xmlEntity;
        this.stringWriter = stringWriter;
    }

    @SuppressWarnings("unchecked")
    public <T extends ComponentBase> ComponentBase Load(Entity ent, Element el) {

        ComponentBase component = null;
        String type = el.get("Type");

        Class<T> cls;
        try {
            cls = (Class<T>) Class.forName(type);

            component = xmlEntity.callAddUnsafe(ent, cls).Load(el);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return component;
    }

    public <T extends ComponentBase> T Load(Entity ent, String componentStr) throws Exception {
        XmlReader.Element el = xmlReader.parse(componentStr);

        String type = el.get("Type");

        @SuppressWarnings("unchecked")
        Class<T> cls = (Class<T>) Class.forName(type);

        ComponentBase component = xmlEntity.callAddUnsafe(ent, cls).Load(el);
        return (T) component;
    }

    public String Save(ComponentBase comp) throws Exception {
        stringWriter.getBuffer().setLength(0);
        comp.Save(xmlWriter);
        return stringWriter.toString();
    }

    public void Save(ComponentBase value, XmlWriter element) {
        try {
            value.Save(xmlWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
