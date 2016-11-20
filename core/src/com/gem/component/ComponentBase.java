package com.gem.component;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ui.ComponentUIButton;
import com.gem.component.ui.ComponentUIButtonGroup;
import com.gem.component.ui.ComponentUICheckBox;
import com.gem.component.ui.ComponentUIContainer;
import com.gem.component.ui.ComponentUIDialog;
import com.gem.component.ui.ComponentUIHorizontalGroup;
import com.gem.component.ui.ComponentUIImage;
import com.gem.component.ui.ComponentUIImageButton;
import com.gem.component.ui.ComponentUILabel;
import com.gem.component.ui.ComponentUILayout;
import com.gem.component.ui.ComponentUIList;
import com.gem.component.ui.ComponentUIProgressBar;
import com.gem.component.ui.ComponentUIScrollPane;
import com.gem.component.ui.ComponentUISelectBox;
import com.gem.component.ui.ComponentUISlider;
import com.gem.component.ui.ComponentUISplitPane;
import com.gem.component.ui.ComponentUIStack;
import com.gem.component.ui.ComponentUIStage;
import com.gem.component.ui.ComponentUITable;
import com.gem.component.ui.ComponentUITextArea;
import com.gem.component.ui.ComponentUITextButton;
import com.gem.component.ui.ComponentUITextField;
import com.gem.component.ui.ComponentUITouchpad;
import com.gem.component.ui.ComponentUITree;
import com.gem.component.ui.ComponentUIVerticalGroup;
import com.gem.component.ui.ComponentUIWindow;
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
	private static int UniqueId = 0;
	protected final ComponentFactory ComponentFactory;
	protected final ComponentSpokesman ComponentSpokesman;
	protected final Gem Ng;
	@Getter
	protected Entity Owner;
	@Getter
	protected Class<?> SubType;
	@Getter
	protected Class<?> Type;
	/**
	 * Variable holds if the component is active or not.
	 */
	@Getter
	protected boolean Enable = true;
	/**
	 * The unique id of the current component. First is guaranteed to be 0.
	 */
	@Getter
	protected int Id;

	/**
	 * Create a BaseComponent with an unique id.
	 */
	public ComponentBase(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		this.ComponentSpokesman = _ComponentSpokesman;
		Ng = ng;
		Type = this.getClass();
		SubType = this.getClass().getSuperclass();
		Owner = ent;
		Id = UniqueId++;
		ComponentFactory = factory;
		reinit();
	}

	public <T extends ComponentBase> T remove() {
		Owner.removeComponent(this.getClass(), Id);
		return (T) this;
	}

	public void reset() {
	}

	/**
	 * Set this component to enabled or disabled.
	 *
	 * @param Enable
	 *            The state of working of the component.
	 */
	public <T extends ComponentBase> T setEnabled(boolean Enable) {
		this.Enable = Enable;
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
		Owner = ent;
		return (T) this;
	}

	protected abstract void visitComponent(ComponentBase component, ComponentFactory factory);
}
