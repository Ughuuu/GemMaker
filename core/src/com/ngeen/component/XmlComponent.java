package com.ngeen.component;

import java.io.StringWriter;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

/**
 * @hidden
 * @author Dragos
 *
 */
public class XmlComponent {
	public XmlComponent(){		
	}
	
	public void Save(ComponentBase comp, XmlWriter element) throws Exception{
		comp.Save(element);
	}
	
	public void Load(ComponentBase comp, XmlReader.Element element) throws Exception{
		comp.Load(element);
	}
}
