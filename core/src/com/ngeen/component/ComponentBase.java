package com.ngeen.component;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ui.*;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

/**
 * The base for all Components. Has to be implemented.
 *
 * @author Dragos
 */
public abstract class ComponentBase {
    private static int _UniqueId = 0;
    protected final ComponentFactory _ComponentFactory;
    protected final ComponentSpokesman _ComponentSpokesman;
    protected final Ngeen _Ng;
    protected Entity _Owner;
    protected Class<?> _SubType;
    protected Class<?> _Type;
    /**
     * Variable holds if the component is active or not.
     */
    protected boolean Enable = true;
    /**
     * The unique id of the current component. First is guaranteed to be 0.
     */
    protected int Id;

    /**
     * Create a BaseComponent with an unique id.
     */
    public ComponentBase(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        this._ComponentSpokesman = _ComponentSpokesman;
        _Ng = ng;
        _Type = this.getClass();
        _SubType = this.getClass().getSuperclass();
        _Owner = ent;
        Id = _UniqueId++;
        _ComponentFactory = factory;
        reinit();
    }

    /**
     * Get if the current object is enabled or not.
     *
     * @return The state of the variable Enable.
     */
    public final boolean getEnabled() {
        return Enable;
    }

    /**
     * Set this component to enabled or disabled.
     *
     * @param Enable The state of working of the component.
     */
    public <T extends ComponentBase> T setEnabled(boolean Enable) {
        this.Enable = Enable;
        return (T) this;
    }

    /**
     * Get the id of the object.
     */
    public final int getId() {
        return Id;
    }

    public Entity getOwner() {
        return _Owner;
    }

    protected <T extends ComponentBase> T setOwner(Entity ent) {
        _Owner = ent;
        return (T) this;
    }

    public Class<?> getSubType() {
        return _SubType;
    }

    public Class<?> getType() {
        return _Type;
    }

    public <T extends ComponentBase> T remove() {
        _Owner.removeComponent(this.getClass(), Id);
        return (T) this;
    }

    public void reset() {
    }

    protected void destroyed() {
    }

    protected abstract ComponentBase Load(XmlReader.Element element) throws Exception;

    /**
     * Implement only what you need from these.
     *
     * @param component
     */
    protected void notiftyParented(Entity parent) {
    }

    protected void notifyDeparented(Entity parent) {
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

    protected abstract void visitComponent(ComponentBase component, ComponentFactory factory);
}
