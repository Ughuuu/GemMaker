package gemengine.system.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.MarkerManager;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.InstanceBinding;

import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
/**
 * A type manager handles types, instantiates them and copies them.
 * 
 * @author Dragos
 *
 * @param <T>
 */
public abstract class TypeManager<T> {
	protected final Map<String, T> types;
	private Injector injector;
	private final List<String> removeList;
	private final List<Class<? extends T>> copyList;
	private final ObjectMapper objectMapper;

	public static boolean extendsType(Class<?> type, Class<?> extendsType) {
		if (type == null || type.equals(Object.class)) {
			return false;
		}
		return type.equals(extendsType) || extendsType(type.getSuperclass(), extendsType);
	}

	/**
	 * Construct a new Type Manager with the given generic class. Used because of
	 * type erasure.
	 * 
	 * @param type
	 */
	public TypeManager(Class<T> type) {
		types = new HashMap<String, T>();
		copyList = new ArrayList<Class<? extends T>>();
		removeList = new ArrayList<String>();
		objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.PUBLIC_ONLY);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Get a type of given name. The name is the type class name.
	 * 
	 * @param type
	 *            The type name
	 * @return The type requested or null.
	 */
	public <U extends T> U getType(String type) {
		return (U) types.get(type);
	}
	
	public List<String> getAllSystems(){
		return new ArrayList<String>(types.keySet());
	}

	/**
	 * Inject a given type.
	 * 
	 * @param type
	 *            The type to inject.
	 * @return The injected type.
	 * @throws Exception
	 */
	public <T> T inject(Class<T> type) throws Exception {
		return injector.getInstance(type);
	}

	/**
	 * Event called on start. This instantiates the types given at start.
	 */
	public void onInit() {
		doCopyLogic(new ArrayList<>());
	}

	/**
	 * Event called on update. This instantiates the types that were added between
	 * last update event. This does not instantiate the types that are excluded.
	 * 
	 * @param delta
	 */
	public void onUpdate(float delta) {
		doCopyLogic(getExcludeList());
		for (String type : removeList) {
			T element = types.remove(type);
			elementDelete(element);
			doMapping();
		}
		removeList.clear();
	}

	/**
	 * Remove a given type. This occurs on onUpdate event. It is not instant.
	 * 
	 * @param type
	 */
	public void removeType(String type) {
		removeList.add(type);
	}

	/**
	 * Copy a given type. This occurs on onUpdate event. It is not instant. Use this
	 * for add event also.
	 * 
	 * @param typeClass
	 */
	public <U extends T> void replaceType(Class<U> typeClass) {
		copyList.add(typeClass);
	}

	/**
	 * Get constructor dependencies of a class type.
	 * 
	 * @param type
	 *            the class type to give dependencies for
	 * @return A list of classes that this has defined in it's constructor
	 */
	public List<Class<? extends T>> getDepends(Class<?> type) {
		List<Class<? extends T>> dependenciesAsClasses = new ArrayList<Class<? extends T>>();
		InjectionPoint injectionPoint = null;
		try {
			injectionPoint = InjectionPoint.forConstructorOf(type);
		} catch (Exception e) {
			return dependenciesAsClasses;
		}
		List<Dependency<?>> dependencies = injectionPoint.getDependencies();
		for (val dependency : dependencies) {
			Class<?> dependencyClass = dependency.getKey().getTypeLiteral().getRawType();
			if (extendsType(dependencyClass, classType)) {
				dependenciesAsClasses.add((Class<? extends T>) dependencyClass);
			}
		}
		return dependenciesAsClasses;
	}

	/**
	 * Add all types from constructor of type to existing types list.
	 * 
	 * @param existingTypes
	 * @param type
	 */
	public void addAll(List<Class<? extends T>> existingTypes, Class<? extends T> type) {
		InjectionPoint injectionPoint = InjectionPoint.forConstructorOf(type);
		List<Dependency<?>> dependencies = injectionPoint.getDependencies();
		for (val dependency : dependencies) {
			Class<?> dependencyClass = dependency.getKey().getTypeLiteral().getRawType();
			if (extendsType(dependencyClass, classType)) {
				if (existingTypes.contains(dependencyClass)) {
					existingTypes.remove(dependencyClass);
				}
				existingTypes.add((Class<? extends T>) dependencyClass);
				addAll(existingTypes, (Class<? extends T>) dependencyClass);
			}
		}
	}

	private List<T> addInstances() {
		for (val key : injector.getAllBindings().entrySet()) {
			Class<?> type = key.getKey().getTypeLiteral().getRawType();
			final Binding<?> value = key.getValue();
			if (extendsType(type, classType) && (value instanceof InstanceBinding)) {
				InstanceBinding instanceBinding = (InstanceBinding) value;
				T instance = (T) instanceBinding.getInstance();
				types.put(type.getName(), instance);
			}
		}
		return null;
	}

	private void copyElement(T oldObject, T newObject) {
		try {
			if (oldObject != null) {
				String oldTypeData;
				oldTypeData = objectMapper.writeValueAsString(oldObject);
				objectMapper.readerForUpdating(newObject).readValue(oldTypeData);
				elementCopy(oldObject, newObject);
			} else {
				elementAdd(newObject);
			}
		} catch (Throwable t) {
			log.warn(MarkerManager.getMarker("gem"), "System Manager copy element", t);
		}
	}

	private void doCopyLogic(List<Class<? extends T>> excludeList) {
		List<Class<? extends T>> toInstantiateList = getAllSystems(copyList);
		Collections.reverse(toInstantiateList);
		List<Class<? extends T>> toRemoveList = new ArrayList<Class<? extends T>>(toInstantiateList);
		toRemoveList.removeAll(excludeList);
		Map<String, T> oldTypes = new HashMap<String, T>();
		Map<String, T> newTypes = new HashMap<String, T>();
		// remove old types
		for (val copyItem : toRemoveList) {
			T oldType = types.remove(copyItem.getName());
			if (oldType != null) {
				oldTypes.put(oldType.getClass().getName(), oldType);
			}
		}
		doMapping();
		// instantiate new types
		for (val copyItem : toInstantiateList) {
			if (types.containsKey(copyItem.getName())) {
				continue;
			}
			try {
				T type = inject(copyItem);
				newTypes.put(type.getClass().getName(), type);
				types.put(type.getClass().getName(), type);
				addInstances();
				doMapping();
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "System Manager inject", t);
			}
		}
		// deserialize old types into new types
		for (val copyItem : toRemoveList) {
			String key = copyItem.getName();
			T oldElement = oldTypes.get(key);
			T newElement = newTypes.get(key);
			if (newElement != null) {
				copyElement(oldElement, newElement);
			}
		}
		copyList.clear();
	}

	private List<Class<? extends T>> getAllSystems(List<Class<? extends T>> types) {
		List<Class<? extends T>> toInstantiate = new ArrayList<Class<? extends T>>();
		for (val cls : types) {
			if (toInstantiate.contains(cls)) {
				toInstantiate.remove(cls);
			}
			toInstantiate.add(cls);
			addAll(toInstantiate, cls);
		}
		return toInstantiate;
	}

	protected void doMapping() {
		injector = Guice.createInjector(getModule());
	}

	protected abstract void elementAdd(T element);

	protected abstract void elementCopy(T oldElement, T newElement);

	protected abstract void elementDelete(T element);

	protected abstract List<Class<? extends T>> getExcludeList();

	protected abstract Module getModule();
}
