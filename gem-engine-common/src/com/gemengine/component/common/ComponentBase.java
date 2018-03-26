package com.gemengine.component.common;

import java.util.List;

/**
 * The basic ComponentBase type. This needs to be extended by components that will
 * be used in the engine. Components hold data only, and the logic is performed by
 * {@link com.gemengine.system.base.SystemBase} or others that extend that. If
 * you need to do logic in the component, {@link com.google.inject.Inject} the
 * component with a system.
 *
 */
public abstract class ComponentBase {
    private boolean enable = true;

    private ComponentBase parent;

    private List<ComponentBase> children;

    public ComponentBase() {
    }

    public void onCreate() {}

    public void onDestroy() {}

    public boolean getEnable(){
        return enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public ComponentBase getParent() {
        return parent;
    }

    public void setParent(ComponentBase parent) {
        this.parent = parent;
    }

    public List<ComponentBase> getChildren() {
        return children;
    }
}
