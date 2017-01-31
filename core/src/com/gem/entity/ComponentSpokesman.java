package com.gem.entity;

import com.gem.engine.UIFactory;
import lombok.Getter;

public class ComponentSpokesman {
    @Getter
    private final UIFactory UIBuilder;

    public ComponentSpokesman(UIFactory _UIBuilder) {
        this.UIBuilder = _UIBuilder;
    }
}
