package com.gem.entity;

import com.gem.action.Command;
import com.gem.action.CommandFactory;
import com.gem.component.ComponentFactory;
import com.gem.debug.Debugger;
import com.gem.engine.EngineInfo;
import com.gem.engine.TypeObservable;
import com.gem.systems.SystemBase;
import com.gem.systems.SystemConfiguration;

import java.util.*;
import java.util.Map.Entry;

/**
 * @composed 1 has * Entity
 */
public class EntityFactory extends TypeObservable<Entity> {
    protected final XmlEntity xmlSave;
    private final ComponentFactory componentFactory;
    private final ComponentSpokesman componentSpokesman;
    private final Set<Entity> emptySet;
    private Entity[] entityCache;
    private int entityCacheIndex = 0;
    private Map<Integer, Entity> entityMap;
    private Map<String, Integer> entityNameMap;
    private Map<Comparator<Entity>, Map<BitSet, Set<Entity>>> entityTrees;

    public EntityFactory(ComponentFactory _ComponentFactory, XmlEntity xmlSave,
                         ComponentSpokesman _ComponentSpokesman) {
        this.componentFactory = _ComponentFactory;
        componentFactory.addObserver(new CameraObserver(this));
        entityCache = new Entity[EngineInfo.EntitiesCache];
        entityMap = new HashMap<Integer, Entity>();
        entityNameMap = new HashMap<String, Integer>();
        entityTrees = new HashMap<Comparator<Entity>, Map<BitSet, Set<Entity>>>();
        emptySet = new TreeSet<Entity>();
        this.componentSpokesman = _ComponentSpokesman;
        this.xmlSave = xmlSave;
    }

    public void addSystem(SystemBase system) {
        Comparator<Entity> comp = system.getComparator();
        SystemConfiguration config = system.getConfiguration();
        if (!entityTrees.containsKey(comp)) {
            entityTrees.put(comp, new HashMap<BitSet, Set<Entity>>());
        }
        for (BitSet bits : config.getConfiguration()) {
            if (!entityTrees.get(comp).containsKey(bits)) {
                entityTrees.get(comp).put(bits, new TreeSet<Entity>(comp));
            }
        }
    }

    public void clear() {
        Set<Entity> entArr = new HashSet<Entity>();
        for (Map.Entry<Comparator<Entity>, Map<BitSet, Set<Entity>>> entry : entityTrees.entrySet()) {
            for (Entry<BitSet, Set<Entity>> entities : entry.getValue().entrySet()) {
                entArr.addAll(entities.getValue());
            }
        }
        for (Entity ent : entArr) {
            // ent.remove();
            // ent.clearComponents();
            ent.removeUnsafe();
        }
        entityMap.clear();
        entityNameMap.clear();
        entityCacheIndex = 0;
        componentFactory.clear();
    }

    /**
     * Get entity by it's id.
     *
     * @param id
     * @return
     */
    public Entity getById(int id) {
        return entityMap.get(id);
    }

    /**
     * Get entity by name.
     *
     * @param tag The name of the object.
     * @return
     */
    public Entity getByName(String tag) {
        Integer id = entityNameMap.get(tag);
        if (id == null)
            return null;
        return entityMap.get(id);
    }

    public List<Entity> getEntities() {
        return new ArrayList<Entity>(entityMap.values());
    }

    public Set<Entity> getEntitiesForSystem(SystemBase system) {
        Comparator<Entity> comp = system.getComparator();
        SystemConfiguration config = system.getConfiguration();
        List<BitSet> configs = config.getConfiguration();
        if (configs == null) {
            return emptySet;
        }
        Set<Entity> entSet = new TreeSet<Entity>(comp);

        for (BitSet configuration : configs) {
            Set<Entity> currentSet = entityTrees.get(comp).get(configuration);
            entSet.addAll(currentSet);
        }
        return entSet;
    }

    public Entity loadEntity(String buffer) {
        try {
            return xmlSave.LoadEntity(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Entity makeEntity(final String name) {
        if (entityNameMap.containsKey(name) == true) {
            Debugger.log(Debugger.gem.getCurrentScene());
            Debugger.log("Object already exists " + name + ".");
            return entityMap.get((entityNameMap.get(name)));
        }
        if (EngineInfo.Debug) {
            CommandFactory.factory.doAction(
                    new Command(() -> makeEntity(name), () -> getByName(name).remove(), "makeEntity(" + name + ")"));
        }
        Entity ret;
        if (entityCacheIndex != 0) {// reuse deleted entities
            entityCacheIndex--;
            ret = entityCache[entityCacheIndex];
            ret.reset(name);
        } else {
            ret = new Entity(this, componentFactory, name, componentSpokesman);
        }
        entityMap.put(ret.id, ret);
        entityNameMap.put(ret.name, ret.id);
        if (EngineInfo.Debug) {
            CommandFactory.factory.endAction();
        }
        return ret;
    }

    /**
     * After the reodering happened.
     *
     * @param entity
     * @param entity2
     */
    public void Order(Entity entity, Entity entity2) {
        NotifyReorder(entity, entity2);
    }

    public String saveEntity(Entity ent) {
        try {
            return xmlSave.SaveEntity(ent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void cacheComponents(Entity ent) {
        ent.clearComponents();
    }

    protected Entity create(Entity ent) {
        try {
            String str = xmlSave.SaveEntity(ent);
            return xmlSave.LoadEntity(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void Parented(Entity ent, Entity parent) {
        NotifyParented(ent, parent);
    }

    protected void removeEntity(Entity ent) {
        if (ent == null)
            return;
        treeRemoveObject(ent);
        entityMap.remove(ent.id);
        entityNameMap.remove(ent.name);
        cacheComponents(ent);
        if (entityCacheIndex < EngineInfo.EntitiesCache) {
            entityCache[entityCacheIndex] = ent;
            entityCacheIndex++;
        }
    }

    protected void setEntityName(String oldName, String newName) {
        if (entityNameMap.containsKey(newName) == true) {
            return;
        }
        Integer id = entityNameMap.remove(oldName);
        Entity ent = entityMap.get(id);
        ent.name = newName;
        entityNameMap.put(newName, id);
    }

    protected void treeAddObject(Entity e) {
        for (Entry<Comparator<Entity>, Map<BitSet, Set<Entity>>> EntityTreePair : entityTrees.entrySet()) {
            Map<BitSet, Set<Entity>> EntityTree = EntityTreePair.getValue();

            for (Map.Entry<BitSet, Set<Entity>> entitiesWithClass : EntityTree.entrySet()) {
                BitSet typeArray = (BitSet) e.getConfiguration().clone();
                BitSet entities = entitiesWithClass.getKey();

                typeArray.and(entities);

                if (entities.equals(typeArray)) {
                    entitiesWithClass.getValue().add(e);
                }
            }
        }
    }

    protected void treeRemoveObject(Entity e) {
        for (Entry<Comparator<Entity>, Map<BitSet, Set<Entity>>> EntityTreePair : entityTrees.entrySet()) {
            Map<BitSet, Set<Entity>> EntityTree = EntityTreePair.getValue();

            for (Map.Entry<BitSet, Set<Entity>> entitiesWithClass : EntityTree.entrySet()) {
                BitSet typeArray = (BitSet) e.getConfiguration().clone();
                BitSet entities = entitiesWithClass.getKey();

                typeArray.and(entities);

                if (entities.equals(typeArray)) {
                    entitiesWithClass.getValue().remove(e);
                }

            }
        }
    }
}
