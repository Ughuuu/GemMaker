package gemengine.system.helper;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper function used in every Listener class from com.gemengine.listener
 * package.
 * 
 * @author Dragos
 *
 */
public class ListenerHelper {
	/**
	 * Create a configuration from the given Component types.
	 * 
	 * @param components
	 *            the type classes of the components needed for the
	 *            configuration
	 * @return The configuration of these components
	 */
	public static Set<String> createConfiguration(Class<? extends Component>... components) {
		Set<String> configuration = new HashSet<String>();
		for (Class<? extends Component> component : components) {
			configuration.add(component.getName());
		}
		return configuration;
	}
}
