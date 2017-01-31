package com.gem.component;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ui.*;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;
import lombok.Getter;

/**
 * The base for all Components. Has to be implemented.
 *
 * @author Dragos
 */
public abstract class ComponentBase {
    private static int uniqueId = 0;
    protected final ComponentFactory componentFactory;
    protected final ComponentSpokesman componentSpokesman;
    protected final Gem gem;
    @Getter
    protected Entity owner;
    @Getter
    protected Class<?> subType;
    @Getter
    protected Class<?> type;
    /**
     * Variable holds if the component is active or not.
     */
    @Getter
    protected boolean enable = true;
    /**
     * The unique id of the current component. First is guaranteed to be 0.
     */
    @Getter
    protected int id;

    /**
     * Create a BaseComponent with an unique id.
     */
    public ComponentBase(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        this.componentSpokesman = _ComponentSpokesman;
        this.gem = ng;
        this.type = this.getClass();
        this.subType = this.getClass().getSuperclass();
        this.owner = ent;
        this.id = uniqueId++;
        this.componentFactory = factory;
        reinit();
    }

    public <T extends ComponentBase> T remove() {
        owner.removeComponent(this.getClass(), id);
        return (T) this;
    }

    public void reset() {
    }

    /**
     * Set this component to enabled or disabled.
     *
     * @param Enable The state of working of the component.
     */
    public <T extends ComponentBase> T setEnabled(boolean Enable) {
        this.enable = Enable;
        return (T) this;
    }

    protected void destroyed() {
    }

    protected abstract ComponentBase Load(XmlReader.Element element) throws Exception;

    protected void notifyDeparented(Entity parent) {
    }

    /**
     * Implement only what you need from these.
     *
     * @param component
     */
    protected void notifyParented(Entity parent) {
    }

    protected void notifyWithComponent(ComponentBase component) {
    }

    protected void notifyWithComponent(ComponentCamera component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentMaterial component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentMesh component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentPoint component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentRigid component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentScript component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentSprite component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIButton component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIButtonGroup component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUICheckBox component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIContainer component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIDialog component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIHorizontalGroup component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIImage component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIImageButton component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUILabel component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUILayout component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIList component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIProgressBar component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIScrollPane component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUISelectBox component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUISlider component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUISplitPane component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIStack component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIStage component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUITable component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUITextArea component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUITextButton component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUITextField component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUITouchpad component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUITree component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIVerticalGroup component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentUIWindow component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void notifyWithComponent(ComponentVariable component) {
        notifyWithComponent((ComponentBase) (component));
    }

    protected void reinit() {
    }

    protected abstract void Save(XmlWriter element) throws Exception;

    protected <T extends ComponentBase> T setOwner(Entity ent) {
        owner = ent;
        return (T) this;
    }

    protected abstract void visitComponent(ComponentBase component, ComponentFactory factory);
}
