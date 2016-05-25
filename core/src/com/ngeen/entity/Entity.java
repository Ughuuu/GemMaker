package com.ngeen.entity;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.action.Command;
import com.ngeen.action.CommandFactory;
import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.component.XmlComponent;
import com.ngeen.component.ui.ComponentUIBase;
import com.ngeen.engine.EngineInfo;

import javax.print.attribute.standard.MediaSize;
import java.util.*;
import java.util.Map.Entry;

/**
 * @composed 1 has * ComponentFactory
 */
public class Entity {
    private static final List emptyList = new ArrayList();
    private static int _Unique_id = 0;
    private final ComponentFactory _ComponentFactory;
    private final ComponentSpokesman _ComponentSpokesman;
    private final EntityFactory _EntityFactory;
    protected String _ParentName = "null";
    protected int Id;
    protected String Name;
    protected Entity Parent;
    private List<Entity> _Children;
    private Map<Class<?>, Map<Integer, ComponentBase>> _ComponentMap;
    private BitSet _Configuration = new BitSet(EngineInfo.TotalComponents);
    private int _Order = -1;

    public Entity(EntityFactory entityFactory, ComponentFactory componentBuilder, String name,
                  ComponentSpokesman _ComponentSpokesman) {
        _EntityFactory = entityFactory;
        _ComponentFactory = componentBuilder;
        this.Name = name;
        Id = _Unique_id++;
        _ComponentMap = new HashMap<Class<?>, Map<Integer, ComponentBase>>();
        _Children = new ArrayList<Entity>();
        this._ComponentSpokesman = _ComponentSpokesman;
    }

    public <T extends ComponentBase> T addComponent(Class<T> type) {
        _EntityFactory.treeRemoveObject(this);

        if (!_ComponentFactory.canHaveMultiple(type) && _ComponentMap.containsKey(type)) {
            _EntityFactory.treeAddObject(this);
            return (T) _ComponentMap.get(type).entrySet().iterator().next().getValue();
        }

        if (EngineInfo.Debug) {
            final String finalName = new String(Name);
            final Class<T> finalType = type;
            CommandFactory.factory.doAction(new Command(
                    () -> CommandFactory.factory._Ng.EntityBuilder.getByName(finalName).addComponent(finalType),
                    () -> CommandFactory.factory._Ng.EntityBuilder.getByName(finalName).removeComponent(finalType),
                    "addComponent("+type.getName()+") -> "+Name));
        }

        ComponentBase component = _ComponentFactory.createComponent(type, this);
        Map<Integer, ComponentBase> list = null;
        if (_ComponentMap.containsKey(type) == false) {
            list = new HashMap<Integer, ComponentBase>();
            _ComponentMap.put(type, list);
        } else {
            list = _ComponentMap.get(type);
        }
        list.put(component.getId(), component);

        _Configuration.set(EngineInfo.ComponentIndexMap.get(type));

        _EntityFactory.treeAddObject(this);


        Class<?> cls = component.getClass();
        if (cls.getSuperclass() != ComponentBase.class) {
            addSuperComponent(component, cls.getSuperclass());
        }

        if(EngineInfo.Debug){
            CommandFactory.factory.endAction();
        }
        return (T) component;
    }

    public boolean canParent(Entity e) {
        if (e == this || e == null) {
            return false;
        }
        if (Parent == null) {
            return true;
        }
        return Parent.canParent(e);
    }

    public Entity clearComponents() {
        for (Map.Entry<Class<?>, Map<Integer, ComponentBase>> typeMap : _ComponentMap.entrySet()) {
            Map<Integer, ComponentBase> components = typeMap.getValue();
            Class<?> type = typeMap.getKey();
            for (Map.Entry<Integer, ComponentBase> iter : components.entrySet()) {
                _ComponentFactory.removeComponent(type, iter.getValue());
            }
        }
        _ComponentMap.clear();
        _Configuration.clear();
        return this;
    }

    @Override
    public Entity clone() {
        String objData = _EntityFactory.saveEntity(this);
        Entity ent = _EntityFactory.loadEntity(objData);
        return ent;
    }

    /**
     * Detach the current entity from it's Parent
     *
     * @return the same entity for chaining
     */
    public Entity detachParent() {
        if (Parent != null) {
            Parent._Children.remove(this);
        }
        if (EngineInfo.Debug) {
            final String parentName = new String(Parent.Name);
            final String finalName = new String(Name);
            CommandFactory.factory
                    .doAction(new Command(() -> CommandFactory.factory._Ng.getEntity(finalName).detachParent(),
                            () -> CommandFactory.factory._Ng.getEntity(finalName)
                                    .setParent(CommandFactory.factory._Ng.getEntity(parentName)),
                            "detachParent("+parentName+") <- "+ getName()));
        }
        _ParentName = "";
        _Order = -1;
        if (Parent != null)
            Parent.recountChildren();
        Entity spare = Parent;
        Parent = null;
        _EntityFactory.Parented(this, spare);
        List<ComponentBase> components = getComponents();
        for (ComponentBase comp : components) {
            _ComponentFactory.callDetachParentNotify(this, spare);
        }
        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        return this;
    }

    public List<Entity> getChildren() {
        return _Children;
    }

    public <T extends ComponentBase> T getComponent(Class<T> type) {
        Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
        if (componentMap == null)
            return null;
        return (T) componentMap.entrySet().iterator().next().getValue();
    }

    public <T extends ComponentBase> T getComponent(Class<T> type, int id) {
        Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
        if (componentMap == null)
            return null;
        return (T) componentMap.get(id);
    }

    public List<ComponentBase> getComponents() {
        List<ComponentBase> ret = new ArrayList<ComponentBase>();
        for (Entry<Class<?>, Map<Integer, ComponentBase>> componentMap : _ComponentMap.entrySet()) {
            Collection<ComponentBase> col = componentMap.getValue().values();
            Class<?> cls = componentMap.getKey();
            for (ComponentBase c : col) {
                if (cls == c.getType())
                    ret.add(c);
            }
        }
        return ret;
    }

    public <T extends ComponentBase> List<T> getComponents(Class<T> type) {
        Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
        if (componentMap == null)
            return emptyList;
        return new ArrayList<T>((Collection<? extends T>) (componentMap.values()));
    }

    public Set<Class<?>> getComponentTypes() {
        return _ComponentMap.keySet();
    }

    public BitSet getConfiguration() {
        return _Configuration;
    }

    public int getId() {
        return Id;
    }

    public Entity getLastParent() {
        Entity ent = getLastParentInternal();
        if (ent == this) {
            return null;
        }
        return ent;
    }

    public String getName() {
        return Name;
    }

    public Entity setName(String name) {
        if (Name.equals("~CAMERA") || Name.equals("~UICAMERA"))
            return this;
        if (name.length() == 0) {
            name = "~";
        }
        // set this for do/undo
        if (EngineInfo.Debug) {
            final String nameCopy = new String(Name);
            final String cpyName2 = new String(name);
            CommandFactory.factory
                    .doAction(new Command(() -> CommandFactory.factory._Ng.getEntity(nameCopy).setName(cpyName2),
                            () -> CommandFactory.factory._Ng.getEntity(cpyName2).setName(nameCopy),
                            "setName("+name+")"));
        }

        _EntityFactory.setEntityName(this.Name, name);
        List<ComponentBase> comps = getComponents();
        for (ComponentBase comp : comps) {
            if (comp instanceof ComponentUIBase) {
                _ComponentFactory.changeName((ComponentUIBase) comp, name);
            }
        }

        // this signals end of do/undo
        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        return this;
    }

    public int getOrder() {
        return _Order;
    }

    protected void setOrder(int newOrder) {
        // if the order is right, do nothing.
        if (_Order == newOrder)
            return;
        Collections.swap(_Children, _Order, newOrder);
        _EntityFactory.Order(this, _Children.get(_Order));
        _Children.get(_Order)._Order = newOrder;
        _Children.get(newOrder)._Order = _Order;
    }

    public Entity getParent() {
        return Parent;
    }

    public Entity setParent(Entity ent) {
        if (!canParent(ent))
            return this;
        // set this for do/undo
        if (EngineInfo.Debug) {
            final String parentName = new String(ent.getName());
            final String nameCopy = new String(Name);
            CommandFactory.factory
                    .doAction(new Command(() -> CommandFactory.factory._Ng.getEntity(nameCopy).setParent(parentName),
                            () -> CommandFactory.factory._Ng.getEntity(nameCopy).detachParent(),
                            "setParent("+parentName+") <- "+nameCopy));
        }

        Parent = ent;
        _ParentName = ent.Name;


        Parent._Children.add(this);
        this._Order = Parent._Children.size() - 1;
        _EntityFactory.Parented(this, ent);
        List<ComponentBase> components = getComponents();
        for (ComponentBase comp : components) {
            _ComponentFactory.callSetParentNotifty(this, Parent);
        }
        // set this to signal end of do/undo
        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        return this;
    }

    public Entity setParent(String name) {
        Entity ent = _EntityFactory.getByName(name);
        if (ent == null)
            return this;
        return setParent(ent);
    }

    /**
     * Find out if this entity has a component of a specified type.
     *
     * @param type The type of the component as a class. Example :
     *             ComponentPoint.class
     * @return If this entity has a component of a specified type or not.
     */
    public <T extends ComponentBase> boolean hasComponent(Class<T> type) {
        return _ComponentMap.containsKey(type);
    }

    /**
     * Find out if this entity has a parent.
     *
     * @return If this entity has a parent or not
     */
    public boolean hasParent() {
        return Parent != null;
    }

    public ComponentBase loadComponent(String serializedComponent) {
        ComponentBase comp = null;
        try {
            comp = _EntityFactory.xmlSave._XmlComponent.Load(this, serializedComponent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return comp;
    }

    /**
     * Remove this entity from the scene.
     */
    public void remove() {
        if (Name.equals("~CAMERA") || Name.equals("~UICAMERA"))
            return;

        // do this to remember the actions we will do, for do/undo
        if (EngineInfo.Debug) {
            final String saved = _EntityFactory.saveEntity(this);
            CommandFactory.factory
                    .doAction(new Command(() -> CommandFactory.factory._Ng.EntityBuilder.getByName(Name).remove(),
                            () -> CommandFactory.factory._Ng.EntityBuilder.loadEntity(saved),
                            "remove("+Name+")"));
        }

        if (Parent != null)
            detachParent();
        for (; _Children.size() != 0; ) {
            _Children.get(0).detachParent();
        }
        // this signals end of do/undo
        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        // clearComponents();
        _EntityFactory.removeEntity(this);
    }

    public <T extends ComponentBase> void removeComponent(Class<T> type) {
        _EntityFactory.treeRemoveObject(this);
        while(_ComponentMap.get(type)!= null && _ComponentMap.get(type).entrySet().iterator().hasNext()){
            Map.Entry<Integer, ComponentBase> iter = _ComponentMap.get(type).entrySet().iterator().next();
            removeComponentRecursively((Class<T>) iter.getValue().getType(), iter.getKey());
        }
        _EntityFactory.treeAddObject(this);
    }

    public <T extends ComponentBase> void removeComponent(Class<T> type, int id) {
        _EntityFactory.treeRemoveObject(this);
        Map<Integer, ComponentBase> components = _ComponentMap.get(type);
        if (components == null) {
            _EntityFactory.treeAddObject(this);
            return;
        }

        ComponentBase comp = components.get(id);

        removeComponentRecursively((Class<T>) comp.getType(), comp.getId());
        _EntityFactory.treeAddObject(this);
    }

    public <T extends ComponentBase> String saveComponent(Class<T> type) {
        String serialized = null;
        try {
            serialized = _EntityFactory.xmlSave._XmlComponent.Save(getComponent(type));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return serialized;
    }

    private Entity getLastParentInternal() {
        if (Parent == null) {
            return this;
        }
        return Parent.getLastParentInternal();
    }

    private void recountChildren() {
        int prev = -1;
        for (int i = 0, j = 0; i < _Children.size(); i++, j++) {
            Entity ent = _Children.get(i);
            int act = ent._Order;
            if (act - prev != -1) {
                ent._Order--;
            }
            prev = act;
        }
    }

    private <T extends ComponentBase> void removeComponentRecursively(Class<T> type, int id) {
        Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
        if (componentMap == null || !componentMap.containsKey(id)) {
            return;
        }
        ComponentBase component = componentMap.remove(id);
        _ComponentFactory.removeComponent(type, component);

        if (componentMap.size() == 0) {
            _Configuration.clear(EngineInfo.ComponentIndexMap.get(type));
            _ComponentMap.remove(type);
        }

        if (type.getSuperclass() != ComponentBase.class) {
            removeComponentRecursively((Class<T>) type.getSuperclass(), id);
        }
    }

    protected <T extends ComponentBase> T addSuperComponent(T component, Class<?> type) {
        _EntityFactory.treeRemoveObject(this);

        if (!_ComponentFactory.canHaveMultiple(type) && _ComponentMap.containsKey(type)) {
            _EntityFactory.treeAddObject(this);
            return (T) _ComponentMap.get(type).entrySet().iterator().next().getValue();
        }

        _ComponentFactory.insertSuperComponent(component);

        Map<Integer, ComponentBase> list = null;
        if (_ComponentMap.containsKey(type) == false) {
            list = new HashMap<Integer, ComponentBase>();
            _ComponentMap.put(type, list);
        } else {
            list = _ComponentMap.get(type);
        }
        list.put(component.getId(), component);

        _Configuration.set(EngineInfo.ComponentIndexMap.get(type));

        _EntityFactory.treeAddObject(this);

        if (type.getSuperclass() != ComponentBase.class) {
            addSuperComponent(component, type.getSuperclass());
        }

        return component;
    }

    protected void Load(Element element, XmlComponent _XmlComponent) throws Exception {
        _ParentName = element.get("Parent");
        _Order = element.getInt("Order");
        for (Element el : element.getChildrenByName("Component")) {
            try {
                String type = el.get("Type");

                Class<?> cls = Class.forName(type);

                _XmlComponent.Load(this, el);
            } catch (Exception exp) {
                // Debugger.log(exp);
                exp.printStackTrace();
            }
        }
    }

    /**
     * This may generate crash
     */
    protected void removeUnsafe() {
        if (Parent != null)
            detachParent();
        for (; _Children.size() != 0; ) {
            _Children.get(0).detachParent();
        }
        // clearComponents();
        _EntityFactory.removeEntity(this);
    }

    protected void reset(String name) {
        Id = _Unique_id++;
        this.Name = name;
    }

    protected void Save(XmlWriter element, XmlComponent _XmlComponent) throws Exception {
        element.element("Entity").attribute("Name", Name).attribute("Parent", _ParentName).attribute("Order", _Order);
        for (Map.Entry<Class<?>, Map<Integer, ComponentBase>> ComponentsIndexMap : _ComponentMap.entrySet()) {
            for (Map.Entry<Integer, ComponentBase> Components : ComponentsIndexMap.getValue().entrySet()) {
                if (ComponentsIndexMap.getKey() == Components.getValue().getType())
                    _XmlComponent.Save(Components.getValue(), element);
            }
        }
        element.pop();
    }
}
