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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public abstract class TypeManager<T> {
	private Injector injector;
	protected final Map<String, T> types;
	private final List<Class<?>> addList;
	private final List<String> removeList;
	private final ObjectMapper objectMapper;

	public TypeManager() {
		types = new HashMap<String, T>();
		addList = new ArrayList<Class<?>>();
		removeList = new ArrayList<String>();
		objectMapper = new ObjectMapper();
		doMapping();
	}

	@SuppressWarnings("unchecked")
	public void addType(Class<? extends T> typeClass) {
		addList.add(typeClass);
	}
	
	public <U extends T> void replaceType(Class<U> typeClass){
		addType(typeClass);
		U system;
		objectMapper.readerForUpdating(system).readValue()
		
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

	@SuppressWarnings("unchecked")
	public <U extends T> U copyInto(Class<U> typeClass, String source) throws Exception {
		JAXBContext context = JAXBContext.newInstance(typeClass);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		U type = unmarshaller.unmarshal(new StreamSource(new StringReader(source)), typeClass).getValue();
		addType(typeClass);
		return type;
	}

	public <U extends T> U getType(String type) {
		return (U) types.get(type);
	}

	public void removeType(String type) {
		removeList.add(type);
	}

	public void onUpdate(float delta) {
		for (Class<?> addingType : addList) {
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
	}

	protected void doMapping() {
		injector = Guice.createInjector(getModule());
	}

	protected abstract void elementAdd(T element);

	protected abstract void elementDelete(T element);

	protected abstract Module getModule();
}
