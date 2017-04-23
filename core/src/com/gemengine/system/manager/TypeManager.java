package com.gemengine.system.manager;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public abstract class TypeManager<T> {
	private Injector injector;
	protected final Map<String, T> types;
	private final List<Class<?>> addList;
	private final List<String> removeList;
	private final List<Class<?>> copyList;
	private final ObjectMapper objectMapper;

	public TypeManager() {
		types = new HashMap<String, T>();
		addList = new ArrayList<Class<?>>();
		copyList = new ArrayList<Class<?>>();
		removeList = new ArrayList<String>();
		objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.NON_PRIVATE);
		doMapping();
	}

	public void addType(Class<? extends T> typeClass) {
		addList.add(typeClass);
	}

	public String copyFrom(String typeName) throws Exception {
		T type = getType(typeName);
		Class<?> typeClass = type.getClass();
		removeType(typeName);
		JAXBContext context = JAXBContext.newInstance(typeClass);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		@SuppressWarnings("unchecked")
		JAXBElement<T> rootElement = new JAXBElement<T>(new QName("", typeName), (Class<T>) typeClass, type);
		StringWriter writer = new StringWriter();
		marshaller.marshal(rootElement, new PrintWriter(writer));
		return writer.toString();
	}

	public <U extends T> U copyInto(Class<U> typeClass, String source) throws Exception {
		JAXBContext context = JAXBContext.newInstance(typeClass);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		U type = unmarshaller.unmarshal(new StreamSource(new StringReader(source)), typeClass).getValue();
		addType(typeClass);
		return type;
	}

	@SuppressWarnings("unchecked")
	public <U extends T> U getType(String type) {
		return (U) types.get(type);
	}

	public void onUpdate(float delta) {
		for (Class<?> addingType : addList) {
			@SuppressWarnings("unchecked")
			T type = (T) injector.getInstance(addingType);
			types.put(addingType.getName(), type);
			doMapping();
			elementAdd(type);
		}
		addList.clear();
		for (String type : removeList) {
			T element = types.remove(type);
			doMapping();
			elementDelete(element);
		}
		removeList.clear();
		for (Class<?> copyingType : copyList) {
			@SuppressWarnings("unchecked")
			T type = (T) injector.getInstance(copyingType);
			T oldType = types.get(copyingType.getName());
			if (oldType != null) {
				String oldTypeData;
				try {
					oldTypeData = objectMapper.writeValueAsString(oldType);
					objectMapper.readerForUpdating(type).readValue(oldTypeData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			types.put(copyingType.getName(), type);
			doMapping();
			elementCopy(oldType, type);
		}
		copyList.clear();
	}

	public void removeType(String type) {
		removeList.add(type);
	}

	public <U extends T> void replaceType(Class<U> typeClass) {
		copyList.add(typeClass);
	}

	protected void doMapping() {
		injector = Guice.createInjector(getModule());
	}

	protected abstract void elementAdd(T element);

	protected abstract void elementCopy(T oldElement, T newElement);

	protected abstract void elementDelete(T element);

	protected abstract Module getModule();
}
