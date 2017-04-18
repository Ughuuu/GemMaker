package com.gem.component.ui;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public abstract class ComponentUIWidget extends ComponentUIBase {

    public ComponentUIWidget(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        // TODO Auto-generated constructor stub
    }

    public void act(float act) {
        getActor().act(act);
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        return this;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        // TODO Auto-generated method stub

    }
    
    public float getHeight(){
    	if(getActor() == null)
    		return 0;
    	return getActor().getHeight();
    }
    
    public float getWidth(){
    	if(getActor() == null)
    		return 0;
    	return getActor().getWidth();
    }
    
    public ComponentUIWidget setHeight(float height){
    	if(getActor() != null)
    		getActor().setHeight(height);
    	return this;
    }
    
    public ComponentUIWidget setWidth(float width){
    	if(getActor() != null)
    		getActor().setWidth(width);
    	return this;
    }

}
