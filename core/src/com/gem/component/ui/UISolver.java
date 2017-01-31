package com.gem.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gem.component.ComponentBase;
import com.gem.engine.Gem;
import com.gem.engine.TypeObserver;
import com.gem.entity.Entity;

import java.util.List;

public class UISolver implements TypeObserver {
    public UISolver() {
    }

    private static ComponentUIBase getUI(Entity ent) {
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

    @Override
    public void Added(ComponentBase obj1) {// , componentFactory factory) {
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
        ComponentUILayout lay = Gem.goUpForComponent(ent, ComponentUILayout.class);
        if (lay != null) {
            lay.add(obj);
            obj.uiParent = lay.getOwner();
            // TODO decide if this is needed here
            lay.get().invalidate();
            return;
        }
        ComponentUIStage stage = Gem.goUpForComponent(ent, ComponentUIStage.class);
        if (stage != null) {
            obj.uiParent = stage.getOwner();
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
        if (ui == null) {
            return;
        }
        // if (ui != null) {
        if (ent.getParent() == null) {// deparented
            if (!ui.getSubType().equals(ComponentUILayout.class) && !ui.getSubType().equals(ComponentUIWidget.class)) {
                return;
            }
            ComponentUILayout lay = Gem.goFindUpWithComponent(parent, ComponentUILayout.class);
            if (lay != null) {
                lay.del(ui);
                ui.uiParent = null;
                return;
            }
            ComponentUIStage stage = Gem.goFindUpWithComponent(parent, ComponentUIStage.class);
            if (stage != null) {
                stage.del(ui);
                ui.uiParent = null;
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
        ComponentUILayout lay = Gem.goUpForComponent(ent, ComponentUILayout.class);
        if (lay != null) {
            lay.del(obj);
            obj.uiParent = null;
            return;
        }
        ComponentUIStage stage = Gem.goUpForComponent(ent, ComponentUIStage.class);
        if (stage != null) {
            stage.del(obj);
            obj.uiParent = null;
            return;
        }
    }

    @Override
    public void Reorder(Entity entity, Entity entity2) {
        if (entity == null || entity2 == null || !(isUI(entity)) || !(isUI(entity2)))
            return;
        ComponentUILayout lay = Gem.goUpForComponent(entity, ComponentUILayout.class);
        if (lay != null && lay == Gem.goUpForComponent(entity2, ComponentUILayout.class)) {
            lay.swap(getUI(entity), getUI(entity2));
            return;
        }
        ComponentUIStage stage = Gem.goUpForComponent(entity, ComponentUIStage.class);
        if (stage != null && stage == Gem.goUpForComponent(entity2, ComponentUIStage.class)) {
            stage.swap(getUI(entity), getUI(entity2));
            stage.stage.getActors().swap(entity.getOrder(), entity2.getOrder());
            return;
        }
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