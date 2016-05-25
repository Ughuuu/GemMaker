package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ngeen.component.ComponentBase;
import com.ngeen.engine.TypeObserver;
import com.ngeen.entity.Entity;

import java.util.List;

public class UISolver implements TypeObserver {

    public UISolver() {

    }

    /**
     * Goes upwards
     *
     * @param ent
     * @param cls
     * @return
     */
    public static <T extends ComponentBase> T goFindLayout(Entity ent, Class<T> cls) {
        if (ent == null) {
            return null;
        }
        if (!ent.hasComponent(cls)) {
            return goUpLayout(ent, cls);
        }
        return ent.getComponent(cls);
    }

    /**
     * Goes upwards
     *
     * @param ent
     * @param cls
     * @return
     */
    public static <T extends ComponentBase> T goUpLayout(Entity ent, Class<T> cls) {
        Entity parent = ent.getParent();
        if (parent == null) {
            return null;
        }
        if (parent != null && !parent.hasComponent(cls)) {
            return goUpLayout(parent, cls);
        }
        return parent.getComponent(cls);
    }

    @Override
    public void Added(ComponentBase obj1) {// , ComponentFactory factory) {
        if (!obj1.getSubType().equals(ComponentUILayout.class) && !obj1.getSubType().equals(ComponentUIWidget.class)) {
            return;
        }
        ComponentUIBase obj = (ComponentUIBase) obj1;
        Actor act = null;
        // if you have a stage, import all children leafs
        if (obj.getType().equals(ComponentUIStage.class)) {
            List<Entity> down = goDownUI(obj.getOwner());
            for (Entity ent : down) {
                obj.add(getUI(ent));
            }
            return;
        }
        Entity ent = obj.getOwner();
        ComponentUILayout lay = goUpLayout(ent, ComponentUILayout.class);
        if (lay != null) {
            lay.add(obj);
            obj._UIParent = lay.getOwner();
            // TODO decide if this is needed here
            lay.get().invalidate();
            return;
        }
        ComponentUIStage stage = goUpLayout(ent, ComponentUIStage.class);
        if (stage != null) {
            obj._UIParent = stage.getOwner();
            stage.add(obj);
            return;
        }
    }

    /**
     * Unused
     */
    @Override
    public void ChangedComponent(ComponentBase obj) {
    }

    @Override
    public void Parented(Entity ent, Entity parent) {
        ComponentUIBase ui = getUI(ent);
        // if (ui != null) {
        if (ent.getParent() == null) {// deparented
            if (!ui.getSubType().equals(ComponentUILayout.class) && !ui.getSubType().equals(ComponentUIWidget.class)) {
                return;
            }
            ComponentUILayout lay = goFindLayout(parent, ComponentUILayout.class);
            if (lay != null) {
                lay.del(ui);
                ui._UIParent = null;
                return;
            }
            ComponentUIStage stage = goFindLayout(parent, ComponentUIStage.class);
            if (stage != null) {
                stage.del(ui);
                ui._UIParent = null;
                return;
            }
        } else {
            Added(ui);
        }
    }

    @Override
    public void Removed(ComponentBase obj1) {
        if (!obj1.getSubType().equals(ComponentUILayout.class) && !obj1.getSubType().equals(ComponentUIWidget.class)) {
            return;
        }
        ComponentUIBase obj = (ComponentUIBase) obj1;
        Entity ent = obj.getOwner();
        ComponentUILayout lay = goUpLayout(ent, ComponentUILayout.class);
        if (lay != null) {
            lay.del(obj);
            obj._UIParent = null;
            return;
        }
        ComponentUIStage stage = goUpLayout(ent, ComponentUIStage.class);
        if (stage != null) {
            stage.del(obj);
            obj._UIParent = null;
            return;
        }
    }

    @Override
    public void Reorder(Entity entity, Entity entity2) {
        if (entity == null || entity2 == null || !(isUI(entity)) || !(isUI(entity2)))
            return;
        ComponentUILayout lay = goUpLayout(entity, ComponentUILayout.class);
        if (lay != null && lay == goUpLayout(entity2, ComponentUILayout.class)) {
            lay.swap(getUI(entity), getUI(entity2));
            return;
        }
        ComponentUIStage stage = goUpLayout(entity, ComponentUIStage.class);
        if (stage != null && stage == goUpLayout(entity2, ComponentUIStage.class)) {
            stage.swap(getUI(entity), getUI(entity2));
            stage._Stage.getActors().swap(entity.getOrder(), entity2.getOrder());
            return;
        }
    }

    private ComponentUIBase getUI(Entity ent) {
        ComponentUIWidget wig = ent.getComponent(ComponentUIWidget.class);
        if (wig != null) {
            return wig;
        }
        ComponentUILayout lay = ent.getComponent(ComponentUILayout.class);
        if (lay != null) {
            return lay;
        }
        return null;
    }

    private boolean isUI(Entity ent) {
        return ent.hasComponent(ComponentUILayout.class) || ent.hasComponent(ComponentUIWidget.class);
    }

    /**
     * Goes downwards on leafs.
     *
     * @param ent
     * @param cls
     * @return
     */
    <T extends ComponentUIBase> List<Entity> goDownUI(Entity ent) {
        List<Entity> children = ent.getChildren();
        for (Entity child : children) {
            if (!child.hasComponent(ComponentUILayout.class) && !child.hasComponent(ComponentUIWidget.class)) {
                children.remove(child);
                List<Entity> subChildren = child.getChildren();
                for (Entity subChild : subChildren) {
                    children.addAll(goDownUI(subChild));
                }
                children.addAll(subChildren);
            }
        }
        return children;
    }
}