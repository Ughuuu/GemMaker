package com.gemengine.listener;

import java.util.Set;

import com.gemengine.component.Component;
import com.gemengine.system.helper.ListenerHelper;

/**
 * Listener interface used for components. Extend this and add yourself as a
 * listener in the {@link com.gemengine.system.ComponentSystem} by calling
 * {@link com.gemengine.system.ComponentSystem#addComponentListener}
 * 
 * @author Dragos
 *
 */
public interface ComponentListener extends PriorityListener{
	/**
	 * The component change type. The cases are described in the enum.
	 * 
	 * @author Dragos
	 *
	 */
	public static enum ComponentChangeType {
		/**
		 * A new component is added to an entity.
		 */
		ADD,
		/**
		 * A component is deleted from an entity.
		 */
		DELETE
	}

	/**
	 * The configuration of this listener. This is used by the
	 * {@link com.gemengine.system.ComponentSystem} to know what components to
	 * give to this listener. To construct this configuration, use
	 * {@link com.gemengine.system.helper.ListenerHelper#createConfiguration}
	 * ex. ListenerHelper.createConfiguration(CameraComponent.class) to listen
	 * only to CameraComponent types or
	 * ListenerHelper.createConfiguration(Component.class) to listen to all
	 * Component types(base class for all components).
	 * 
	 * @return
	 */
	public Set<String> getConfiguration();

	/**
	 * Event called when a component change occurred.
	 * 
	 * @param changeType
	 *            The change type.
	 * @param component
	 *            The component that changed.
	 */
	public <T extends Component> void onChange(ComponentChangeType changeType, T component);

	/**
	 * Called when a component triggered a notify event.
	 * 
	 * @param event
	 *            The event name
	 * @param notifier
	 *            The event starter
	 */
	public <T extends Component> void onNotify(String event, T notifier);

	/**
	 * Called when the component type has changed(eg. the class that represented
	 * the component has changed externally).
	 * 
	 * @param type
	 *            The type that changed.
	 */
	public <T extends Component> void onTypeChange(Class<T> type);
}
