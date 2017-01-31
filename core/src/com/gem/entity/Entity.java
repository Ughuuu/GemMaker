package com.gem.entity;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.action.Command;
import com.gem.action.CommandFactory;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.component.ComponentScript;
import com.gem.component.Script;
import com.gem.component.XmlComponent;
import com.gem.component.ui.ComponentUIBase;
import com.gem.debug.Debugger;
import com.gem.engine.EngineInfo;
import lombok.Getter;
import lombok.val;

import java.util.*;
import java.util.Map.Entry;

/**
 * @composed 1 has * componentFactory
 */
public class Entity {
    private static final List emptyList = new ArrayList();
    private static int uniqueId = 0;
    protected final ComponentFactory componentFactory;
    private final ComponentSpokesman componentSpokesman;
    private final EntityFactory entityFactory;
    protected String _ParentName = "null";
    @Getter
    protected int id;
    @Getter
    protected String name;
    @Getter
    protected Entity parent;
    @Getter
    private List<Entity> children;
    private Map<Class<?>, Map<Integer, ComponentBase>> componentMaps;
    @Getter
    private BitSet configuration = new BitSet(EngineInfo.TotalComponents);
    @Getter
    private int order = -1;
    @Getter
    private boolean saveable = true;

    public Entity(EntityFactory entityFactory, ComponentFactory componentBuilder, String name,
                  ComponentSpokesman componentSpokesman) {
        this.entityFactory = entityFactory;
        this.componentFactory = componentBuilder;
        this.name = name;
        id = uniqueId++;
        componentMaps = new HashMap<Class<?>, Map<Integer, ComponentBase>>();
        children = new ArrayList<Entity>();
        this.componentSpokesman = componentSpokesman;
    }

    public <T extends ComponentBase> T addComponent(Class<T> type) {
        entityFactory.treeRemoveObject(this);

        if (!componentFactory.canHaveMultiple(type) && componentMaps.containsKey(type)) {
            entityFactory.treeAddObject(this);
            return (T) componentMaps.get(type).entrySet().iterator().next().getValue();
        }

        if (EngineInfo.Debug) {
            final String finalName = new String(name);
            final Class<T> finalType = type;
            CommandFactory.factory.doAction(new Command(
                    () -> CommandFactory.factory.gem.entityBuilder.getByName(finalName).addComponent(finalType),
                    () -> CommandFactory.factory.gem.entityBuilder.getByName(finalName).removeComponent(finalType),
                    "addComponent(" + type.getName() + ") -> " + name));
        }

        ComponentBase component = componentFactory.createComponent(type, this);
        Map<Integer, ComponentBase> list = null;
        if (componentMaps.containsKey(type) == false) {
            list = new HashMap<Integer, ComponentBase>();
            componentMaps.put(type, list);
        } else {
            list = componentMaps.get(type);
        }
        list.put(component.getId(), component);

        configuration.set(EngineInfo.ComponentIndexMap.get(type));

        entityFactory.treeAddObject(this);

        Class<?> cls = component.getClass();
        if (cls.getSuperclass() != ComponentBase.class) {
            addSuperComponent(component, cls.getSuperclass());
        }

        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        return (T) component;
    }
    
    public boolean hasChildren(){
    	return !children.isEmpty();
    }

    public boolean canParent(Entity e) {
        if (e == this || e == null) {
            return false;
        }
        if (parent == null) {
            return true;
        }
        return parent.canParent(e);
    }

    public Entity clearComponents() {
        for (Map.Entry<Class<?>, Map<Integer, ComponentBase>> typeMap : componentMaps.entrySet()) {
            Map<Integer, ComponentBase> components = typeMap.getValue();
            Class<?> type = typeMap.getKey();
            for (Map.Entry<Integer, ComponentBase> iter : components.entrySet()) {
                componentFactory.removeComponent(type, iter.getValue());
            }
        }
        componentMaps.clear();
        configuration.clear();
        return this;
    }

    @Override
    public Entity clone() {
        String objData = entityFactory.saveEntity(this);
        Entity ent = entityFactory.loadEntity(objData);
        return ent;
    }

    /**
     * Detach the current entity from it's Parent
     *
     * @return the same entity for chaining
     */
    public Entity detachParent() {
        if (parent != null) {
            parent.children.remove(this);
        }
        if (EngineInfo.Debug) {
            final String parentName = new String(parent.name);
            final String finalName = new String(name);
            CommandFactory.factory
                    .doAction(new Command(() -> CommandFactory.factory.gem.getEntity(finalName).detachParent(),
                            () -> CommandFactory.factory.gem.getEntity(finalName)
                                    .setParent(CommandFactory.factory.gem.getEntity(parentName)),
                            "detachParent(" + parentName + ") <- " + getName()));
        }
        _ParentName = "";
        order = -1;
        if (parent != null)
            parent.recountChildren();
        Entity spare = parent;
        parent = null;
        entityFactory.Parented(this, spare);
        List<ComponentBase> components = getComponents();
        for (ComponentBase comp : components) {
            componentFactory.callDetachParentNotify(this, spare);
        }
        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        return this;
    }

    public <T extends ComponentBase> T getComponent(Class<T> type) {
        Map<Integer, ComponentBase> componentMap = componentMaps.get(type);
        if (componentMap == null)
            return null;
        return (T) componentMap.entrySet().iterator().next().getValue();
    }

    public <T extends ComponentBase> T getComponent(Class<T> type, int id) {
        Map<Integer, ComponentBase> componentMap = componentMaps.get(type);
        if (componentMap == null)
            return null;
        return (T) componentMap.get(id);
    }

    public List<ComponentBase> getComponents() {
        List<ComponentBase> ret = new ArrayList<ComponentBase>();
        for (Entry<Class<?>, Map<Integer, ComponentBase>> componentMap : componentMaps.entrySet()) {
            Collection<ComponentBase> col = componentMap.getValue().values();
            Class<?> cls = componentMap.getKey();
            for (ComponentBase c : col) {
                if (cls == c.getType())
                    ret.add(c);
            }
        }
        return ret;
    }
    
    public <T extends Script> T getScript(Class<T> cls){
    	val scripts = getComponents(ComponentScript.class);
    	for(ComponentScript script : scripts){
    		if(script.getScript() != null 
    				&& script.getScript().getClass().getCanonicalName().equals(cls.getCanonicalName())){ 
    			return (T)(script.getScript());
    		}
    	}
    	return null;
    }

    public <T extends ComponentBase> List<T> getComponents(Class<T> type) {
        val componentMap = componentMaps.get(type);
        if (componentMap == null)
            return emptyList;
        return new ArrayList<T>((Collection<? extends T>) (componentMap.values()));
    }

    public Set<Class<?>> getComponentTypes() {
        return componentMaps.keySet();
    }

    public Entity getLastParent() {
        Entity ent = getLastParentInternal();
        return ent;
    }

    /**
     * Find out if this entity has a component of a specified type.
     *
     * @param type The type of the component as a class. Example :
     *             ComponentPoint.class
     * @return If this entity has a component of a specified type or not.
     */
    public <T extends ComponentBase> boolean hasComponent(Class<T> type) {
        return componentMaps.containsKey(type);
    }

    /**
     * Find out if this entity has a parent.
     *
     * @return If this entity has a parent or not
     */
    public boolean hasParent() {
        return parent != null;
    }

    public ComponentBase loadComponent(String serializedComponent) {
        ComponentBase comp = null;
        try {
            comp = entityFactory.xmlSave.xmlComponent.Load(this, serializedComponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comp;
    }

    /**
     * Remove this entity from the scene.
     */
    public void remove() {
        // do this to remember the actions we will do, for do/undo
        if (EngineInfo.Debug) {
            final String saved = entityFactory.saveEntity(this);
            CommandFactory.factory
                    .doAction(new Command(() -> CommandFactory.factory.gem.entityBuilder.getByName(name).remove(),
                            () -> CommandFactory.factory.gem.entityBuilder.loadEntity(saved), "remove(" + name + ")"));
        }

        if (parent != null)
            detachParent();
        for (; children.size() != 0; ) {
            children.get(0).detachParent();
        }
        // this signals end of do/undo
        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        // clearComponents();
        entityFactory.removeEntity(this);
    }

    public <T extends ComponentBase> void removeComponent(Class<T> type) {
        entityFactory.treeRemoveObject(this);
        while (componentMaps.get(type) != null && componentMaps.get(type).entrySet().iterator().hasNext()) {
            Map.Entry<Integer, ComponentBase> iter = componentMaps.get(type).entrySet().iterator().next();
            removeComponentRecursively((Class<T>) iter.getValue().getType(), iter.getKey());
        }
        entityFactory.treeAddObject(this);
    }

    public <T extends ComponentBase> void removeComponent(Class<T> type, int id) {
        entityFactory.treeRemoveObject(this);
        Map<Integer, ComponentBase> components = componentMaps.get(type);
        if (components == null) {
            entityFactory.treeAddObject(this);
            return;
        }

        ComponentBase comp = components.get(id);

        removeComponentRecursively((Class<T>) comp.getType(), comp.getId());
        entityFactory.treeAddObject(this);
    }

    public <T extends ComponentBase> String saveComponent(Class<T> type) {
        String serialized = null;
        try {
            serialized = entityFactory.xmlSave.xmlComponent.Save(getComponent(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serialized;
    }

    public Entity setName(String newName) {
        if (newName.length() == 0) {
            newName = "~";
        }
        // set this for do/undo
        if (EngineInfo.Debug) {
            final String nameCopy = new String(name);
            final String cpyName2 = new String(newName);
            CommandFactory.factory.doAction(new Command(
                    () -> CommandFactory.factory.gem.getEntity(nameCopy).setName(cpyName2),
                    () -> CommandFactory.factory.gem.getEntity(cpyName2).setName(nameCopy), "setName(" + newName + ")"));
        }

        entityFactory.setEntityName(this.name, newName);
        List<ComponentBase> comps = getComponents();
        for (ComponentBase comp : comps) {
            if (comp instanceof ComponentUIBase) {
                componentFactory.changeName((ComponentUIBase) comp, newName);
            }
        }

        // this signals end of do/undo
        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        return this;
    }

    public Entity setParent(Entity ent) {
        if (!canParent(ent))
            return this;
        // set this for do/undo
        if (EngineInfo.Debug) {
            final String parentName = new String(ent.getName());
            final String nameCopy = new String(name);
            CommandFactory.factory
                    .doAction(new Command(() -> CommandFactory.factory.gem.getEntity(nameCopy).setParent(parentName),
                            () -> CommandFactory.factory.gem.getEntity(nameCopy).detachParent(),
                            "setParent(" + parentName + ") <- " + nameCopy));
        }

        parent = ent;
        _ParentName = ent.name;

        parent.children.add(this);
        this.order = parent.children.size() - 1;
        entityFactory.Parented(this, ent);
        List<ComponentBase> components = getComponents();
        for (ComponentBase comp : components) {
            componentFactory.callSetParentNotifty(this, parent);
        }
        // set this to signal end of do/undo
        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        return this;
    }

    public Entity setParent(String name) {
        Entity ent = entityFactory.getByName(name);
        if (ent == null)
            return this;
        return setParent(ent);
    }

    private Entity getLastParentInternal() {
        if (parent == null) {
            return this;
        }
        return parent.getLastParentInternal();
    }

    private void recountChildren() {
        int prev = -1;
        for (int i = 0, j = 0; i < children.size(); i++, j++) {
            Entity ent = children.get(i);
            int act = ent.order;
            if (act - prev != -1) {
                ent.order--;
            }
            prev = act;
        }
    }

    private <T extends ComponentBase> void removeComponentRecursively(Class<T> type, int id) {
        Map<Integer, ComponentBase> componentMap = componentMaps.get(type);
        if (componentMap == null || !componentMap.containsKey(id)) {
            return;
        }
        ComponentBase component = componentMap.remove(id);
        componentFactory.removeComponent(type, component);

        if (componentMap.size() == 0) {
            configuration.clear(EngineInfo.ComponentIndexMap.get(type));
            componentMaps.remove(type);
        }

        if (type.getSuperclass() != ComponentBase.class) {
            removeComponentRecursively((Class<T>) type.getSuperclass(), id);
        }
    }

    protected <T extends ComponentBase> T addSuperComponent(T component, Class<?> type) {
        entityFactory.treeRemoveObject(this);

        if (!componentFactory.canHaveMultiple(type) && componentMaps.containsKey(type)) {
            entityFactory.treeAddObject(this);
            return (T) componentMaps.get(type).entrySet().iterator().next().getValue();
        }

        componentFactory.insertSuperComponent(component);

        Map<Integer, ComponentBase> list = null;
        if (componentMaps.containsKey(type) == false) {
            list = new HashMap<Integer, ComponentBase>();
            componentMaps.put(type, list);
        } else {
            list = componentMaps.get(type);
        }
        list.put(component.getId(), component);

        configuration.set(EngineInfo.ComponentIndexMap.get(type));

        entityFactory.treeAddObject(this);

        if (type.getSuperclass() != ComponentBase.class) {
            addSuperComponent(component, type.getSuperclass());
        }

        return component;
    }

    protected void Load(Element element, XmlComponent _XmlComponent) throws Exception {
        try {
            _ParentName = element.get("Parent");
            order = element.getInt("Order");
        } catch (Exception e) {
            order = -1;
            _ParentName = "null";
        }
        for (Element el : element.getChildrenByName("Component")) {
            try {
                String type = el.get("Type");

                //Class<?> cls = Class.forName(type);

                _XmlComponent.Load(this, el);
            } catch (Exception e) {
                Debugger.log("Error on entity " + name);
                e.printStackTrace();
            }
        }
    }
    
    protected void setSaveable(boolean saveable){
    	this.saveable = saveable;
    }

    /**
     * This may generate crash
     */
    protected void removeUnsafe() {
        if (parent != null)
            detachParent();
        for (; children.size() != 0; ) {
            children.get(0).detachParent();
        }
        // clearComponents();
        entityFactory.removeEntity(this);
    }

    protected void reset(String name) {
        id = uniqueId++;
        this.name = name;
    }

    protected void Save(XmlWriter element, XmlComponent _XmlComponent) throws Exception {
        element.element("Entity").attribute("Name", name).attribute("Parent", _ParentName).attribute("Order", order);
        for (Map.Entry<Class<?>, Map<Integer, ComponentBase>> ComponentsIndexMap : componentMaps.entrySet()) {
            for (Map.Entry<Integer, ComponentBase> Components : ComponentsIndexMap.getValue().entrySet()) {
                if (ComponentsIndexMap.getKey() == Components.getValue().getType())
                    _XmlComponent.Save(Components.getValue(), element);
            }
        }
        element.pop();
    }

    protected void setOrder(int newOrder) {
        // if the order is right, do nothing.
        if (order == newOrder)
            return;
        Collections.swap(children, order, newOrder);
        entityFactory.Order(this, children.get(order));
        children.get(order).order = newOrder;
        children.get(newOrder).order = order;
    }
}
