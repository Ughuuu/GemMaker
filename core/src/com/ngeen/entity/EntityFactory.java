package com.ngeen.entity;

import com.ngeen.action.Command;
import com.ngeen.action.CommandFactory;
import com.ngeen.component.ComponentFactory;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.TypeObservable;
import com.ngeen.systems.SystemBase;
import com.ngeen.systems.SystemConfiguration;

import java.util.*;
import java.util.Map.Entry;

/**
 * @composed 1 has * Entity
 */
public class EntityFactory extends TypeObservable<Entity> {
    protected final XmlEntity xmlSave;
    private final ComponentFactory _ComponentFactory;
    private final ComponentSpokesman _ComponentSpokesman;
    private final Set<Entity> _EmptySet;
    private Entity[] _EntityCache;
    private int _EntityCacheIndex = 0;
    private Map<Integer, Entity> _EntityMap;
    private Map<String, Integer> _EntityNameMap;
    private Map<Comparator<Entity>, Map<BitSet, Set<Entity>>> _EntityTrees;

    public EntityFactory(ComponentFactory _ComponentFactory, XmlEntity xmlSave,
                         ComponentSpokesman _ComponentSpokesman) {
        this._ComponentFactory = _ComponentFactory;
        _EntityCache = new Entity[EngineInfo.EntitiesCache];
        _EntityMap = new HashMap<Integer, Entity>();
        _EntityNameMap = new HashMap<String, Integer>();
        _EntityTrees = new HashMap<Comparator<Entity>, Map<BitSet, Set<Entity>>>();
        _EmptySet = new TreeSet<Entity>();
        this._ComponentSpokesman = _ComponentSpokesman;
        this.xmlSave = xmlSave;
    }

    public void addSystem(SystemBase system) {
        Comparator<Entity> comp = system.getComparator();
        SystemConfiguration config = system.getConfiguration();
        if (!_EntityTrees.containsKey(comp)) {
            _EntityTrees.put(comp, new HashMap<BitSet, Set<Entity>>());
        }
        for (BitSet bits : config.getConfiguration()) {
            if (!_EntityTrees.get(comp).containsKey(bits)) {
                _EntityTrees.get(comp).put(bits, new TreeSet<Entity>(comp));
            }
        }
    }

    public void clear() {
        Set<Entity> entArr = new HashSet<Entity>();
        for (Map.Entry<Comparator<Entity>, Map<BitSet, Set<Entity>>> entry : _EntityTrees.entrySet()) {
            for (Entry<BitSet, Set<Entity>> entities : entry.getValue().entrySet()) {
                entArr.addAll(entities.getValue());
            }
        }
        for (Entity ent : entArr) {
            // ent.remove();
            // ent.clearComponents();
            ent.removeUnsafe();
        }
        _EntityMap.clear();
        _EntityNameMap.clear();
        _EntityCacheIndex = 0;
        _ComponentFactory.clear();
    }

    /**
     * Get entity by it's id.
     *
     * @param id
     * @return
     */
    public Entity getById(int id) {
        return _EntityMap.get(id);
    }

    /**
     * Get entity by name.
     *
     * @param tag The name of the object.
     * @return
     */
    public Entity getByName(String tag) {
        Integer id = _EntityNameMap.get(tag);
        if (id == null)
            return null;
        return _EntityMap.get(id);
    }

    public List<Entity> getEntities() {
        return new ArrayList<Entity>(_EntityMap.values());
    }

    public Set<Entity> getEntitiesForSystem(SystemBase system) {
        Comparator<Entity> comp = system.getComparator();
        SystemConfiguration config = system.getConfiguration();
        List<BitSet> configs = config.getConfiguration();
        if (configs == null) {
            return _EmptySet;
        }
        Set<Entity> entSet = new TreeSet<Entity>(comp);

        for (BitSet configuration : configs) {
            Set<Entity> currentSet = _EntityTrees.get(comp).get(configuration);
            entSet.addAll(currentSet);
        }
        return entSet;
    }

    public Entity loadEntity(String buffer) {
        try {
            return xmlSave.LoadEntity(buffer);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Entity makeEntity(final String name) {
        if (_EntityNameMap.containsKey(name) == true) {
            Debugger.log("Object already exists " + name + ".");
            return _EntityMap.get((_EntityNameMap.get(name)));
        }
        if (EngineInfo.Debug) {
            CommandFactory.factory.doAction(new Command(() -> makeEntity(name), () -> getByName(name).remove(), "makeEntity("+name+")"));
        }
        Entity ret;
        if (_EntityCacheIndex != 0) {// reuse deleted entities
            _EntityCacheIndex--;
            ret = _EntityCache[_EntityCacheIndex];
            ret.reset(name);
        } else {
            ret = new Entity(this, _ComponentFactory, name, _ComponentSpokesman);
        }
        _EntityMap.put(ret.Id, ret);
        _EntityNameMap.put(ret.Name, ret.Id);
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
            // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
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
        _EntityMap.remove(ent.Id);
        _EntityNameMap.remove(ent.Name);
        cacheComponents(ent);
        if (_EntityCacheIndex < EngineInfo.EntitiesCache) {
            _EntityCache[_EntityCacheIndex] = ent;
            _EntityCacheIndex++;
        }
    }

    protected void setEntityName(String oldName, String newName) {
        if (_EntityNameMap.containsKey(newName) == true) {
            return;
        }
        Integer id = _EntityNameMap.remove(oldName);
        Entity ent = _EntityMap.get(id);
        ent.Name = newName;
        _EntityNameMap.put(newName, id);
    }

    protected void treeAddObject(Entity e) {
        for (Entry<Comparator<Entity>, Map<BitSet, Set<Entity>>> EntityTreePair : _EntityTrees.entrySet()) {
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
        for (Entry<Comparator<Entity>, Map<BitSet, Set<Entity>>> EntityTreePair : _EntityTrees.entrySet()) {
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
