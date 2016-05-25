package com.ngeen.entity;

import com.ngeen.engine.UIFactory;

public class ComponentSpokesman {
    private final UIFactory _UIBuilder;

    public ComponentSpokesman(UIFactory _UIBuilder) {
        this._UIBuilder = _UIBuilder;
    }

    public UIFactory getUIBuilder() {
        return _UIBuilder;
    }

}
