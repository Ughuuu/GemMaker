package com.gem.systems;

import com.gem.engine.Gem;
import com.gem.entity.Entity;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author Dragos
 * @composed 1 - 1 SystemConfiguration
 */
public abstract class SystemBase {
    private final static Comparator<Entity> _EntityComparator = new EntityComparator();
    public final Gem gem;
    public float deltaTime;
    protected SystemConfiguration config = null;
    protected boolean Enable;

    public SystemBase(Gem ng) {
        config = new SystemConfiguration();
        this.gem = ng;
    }

    public SystemBase(Gem ng, SystemConfiguration conf) {
        Enable = true;
        this.config = conf;
        this.gem = ng;
    }

    public Comparator<Entity> getComparator() {
        return _EntityComparator;
    }

    public SystemConfiguration getConfiguration() {
        return config;
    }

    /**
     * Get the state of enable of the system.
     *
     * @return
     */
    public boolean isEnable() {
        return Enable;
    }

    /**
     * enable or Disable this system.
     *
     * @param Enable boolean holding the next state of the system.
     */
    public void setEnable(boolean Enable) {
        this.Enable = Enable;
    }

    /**
     * Called after update, once per frame.
     */
    public void onAfterUpdate() {
    }

    /**
     * Called before update, once per frame.
     */
    public void onBeforeUpdate() {
    }

    /**
     * Called when this system is destroyed.
     */
    public void onDestroy() {
    }

    /**
     * Called n times per frame, where n is the number of objects it receives.
     */
    public void onUpdate(Entity ent) {
    }
    
    public void onUpdate(Set<Entity> ent) {
    }

    static class EntityComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity o1, Entity o2) {
            if (o1.hashCode() > o2.hashCode())
                return 1;
            if (o1.hashCode() < o2.hashCode())
                return -1;
            return 0;
        }
    }
}
