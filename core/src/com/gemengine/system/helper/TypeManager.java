package com.gemengine.system.helper;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public abstract class TypeManager<T> {
	private Injector injector;
	protected final Map<Class<? extends T>, T> types;

	public TypeManager() {
		types = new HashMap<Class<? extends T>, T>();
		doMapping();
	}

	@SuppressWarnings("unchecked")
	public void addType(Class<? extends T> typeClass) {
		T type = injector.getInstance(typeClass);
		types.put(typeClass, type);
		doMapping();
		elementAdd(type);
	}

	public String copyFrom(Class<? extends T> typeClass) throws Exception {
		T type = getType(typeClass);
		removeType(typeClass);
		JAXBContext context = JAXBContext.newInstance(typeClass);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		@SuppressWarnings("unchecked")
		JAXBElement<T> rootElement = new JAXBElement<T>(new QName("", typeClass.getName()), (Class<T>) typeClass, type);
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
	public <U extends T> U getType(Class<U> typeClass) {
		return (U) types.get(typeClass);
	}

	public void removeType(Class<? extends T> typeClass) {
		T element = types.remove(typeClass);
		doMapping();
		elementDelete(element);
	}

	protected void doMapping() {
		injector = Guice.createInjector(getModule());
	}

	protected abstract void elementAdd(T element);

	protected abstract void elementDelete(T element);

	protected abstract Module getModule();
}
