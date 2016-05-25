package com.ngeen.component;

import com.ngeen.component.ui.*;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.engine.TypeObservable;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <img src=
 * "https://raw.githubusercontent.com/Ughuuu/ngeen/online/core/doc/img/ComponentFactory.png"/>
 *
 * @author Dragos
 * @opt shape node
 * @composed 1 has * ComponentBase
 */
public class ComponentFactory extends TypeObservable<ComponentBase> {
    private final static Map<Class<?>, Boolean> _ComponentMultipleTypes = new HashMap<Class<?>, Boolean>() {
        {
            put(ComponentUIBase.class, true);
            put(ComponentScript.class, true);
            put(ComponentVariable.class, true);
        }
    };
    private final ComponentSpokesman _ComponentSpokesman;
    private final Ngeen _Ng;
    // list not array because can't instantiate array of generic:(
    private Map<Class<?>, List<ComponentBase>> _ComponentCache;
    private Map<Class<?>, Integer> _ComponentCacheIndex;

    public ComponentFactory(Ngeen ng, ComponentSpokesman _ComponentSpokesman) {
        _Ng = ng;
        _ComponentCache = new HashMap<Class<?>, List<ComponentBase>>();
        _ComponentCacheIndex = new HashMap<Class<?>, Integer>();
        this._ComponentSpokesman = _ComponentSpokesman;
    }

    public void callComponentNotify(ComponentUIButton element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIButtonGroup element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUICheckBox element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIContainer element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIDialog element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIHorizontalGroup element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIImage element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIImageButton element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUILabel element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIList element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIProgressBar element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIScrollPane element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUISelectBox element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUISlider element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUISplitPane element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIStack element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIStage element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUITable element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUITextArea element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUITextButton element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUITextField element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUITouchpad element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUITree element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIVerticalGroup element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callComponentNotify(ComponentUIWindow element, ComponentBase base) {
        base.notifyWithComponent(element);
    }

    public void callDetachParentNotify(Entity entity, Entity spare) {
        List<ComponentBase> components = entity.getComponents();
        for (ComponentBase comp : components) {
            comp.notifyDeparented(spare);
        }
    }

    public void callSetParentNotifty(Entity entity, Entity parent) {
        List<ComponentBase> components = entity.getComponents();
        for (ComponentBase comp : components) {
            comp.notiftyParented(parent);
        }
    }

    public boolean canHaveMultiple(Class<?> type) {
        return _ComponentMultipleTypes.containsKey(type);
    }

    public void changeName(ComponentUIBase comp, String name) {

    }

    public void clear() {
        _ComponentCache.clear();
        _ComponentCacheIndex.clear();
    }

    @SuppressWarnings("unchecked")
    public <T extends ComponentBase> T createComponent(Class<?> type, Entity ent) {
        T el = null;
        try {
            Integer size = _ComponentCacheIndex.get(type);
            if (size != null && size > 0) {
                size--;
                List<ComponentBase> list = _ComponentCache.get(type);
                el = (T) list.get(size);
                ((ComponentBase) el).reinit();
                _ComponentCacheIndex.put(type, size);
            } else {
                el = (T) type
                        .getConstructor(Ngeen.class, Entity.class, ComponentFactory.class, ComponentSpokesman.class)
                        .newInstance(_Ng, ent, this, _ComponentSpokesman);
            }
            NotifyAdd(el);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // notifyAllComponents(ent.getComponents(), el);
        return el;
    }

    public void insertSuperComponent(ComponentBase component) {
        // don't track these, as they are already accounted for.
        // but do track them
        // NotifyAdd(component);
    }

    public void notifyAllComponents(List<ComponentBase> components, ComponentBase component) {
        for (ComponentBase element : components) {
            if (element != component) {
                component.visitComponent(element, this);
            }
        }
    }

    public <T extends ComponentBase> void removeComponent(Class<?> type, T component) {
        Integer size = _ComponentCacheIndex.get(type);
        List<ComponentBase> list = _ComponentCache.get(type);
        // construct the cache
        if (list == null) {
            list = new ArrayList<ComponentBase>(EngineInfo.ComponentCache);
            for (int i = 0; i < EngineInfo.ComponentCache; i++) {
                list.add(null);
            }
            _ComponentCache.put(type, list);
            size = 0;
            _ComponentCacheIndex.put(type, size);
            _ComponentCache.put(type, list);
        }
        // add in cache
        if (size < EngineInfo.ComponentCache) {
            _ComponentCacheIndex.put(type, size + 1);
            list.set(size, component);
            component.reset();
        }
        component.destroyed();
        size++;
        NotifyRemove(component);
    }

    public String saveComponent(ComponentBase component, XmlComponent comp) {
        try {
            component.Save(comp.xmlWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
