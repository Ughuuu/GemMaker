package com.ngeen.component;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.asset.Asset;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

public class ComponentMaterial extends ComponentBase {
    private Asset<ShaderProgram> _Shader;
    private String _ShaderName;

    public ComponentMaterial(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
    }

    public ShaderProgram getShader() {
        if (_Shader == null) {
            return null;
        }
        return _Shader.getData();
    }

    public ComponentMaterial setShader(Asset<ShaderProgram> shader) {
        _ShaderName = shader.getFolder() + shader.getPath();
        _Shader = shader;
        return this;
    }

    public ComponentMaterial setShader(String shaderName) {
        _ShaderName = shaderName;
        _Shader = _Ng.Loader.getAsset(shaderName);
        return this;
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        _ShaderName = element.getChildByName("Shader").get("Name");
        setShader(_ShaderName);
        return this;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        element.element("Component").attribute("Type", _Type.getName()).element("Shader")
                .attribute("Name", _ShaderName).pop().pop();
    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        component.notifyWithComponent(this);
    }
}
