package com.gem.systems;

import com.gem.component.ComponentCamera;
import com.gem.engine.Gem;
import com.gem.entity.Entity;
import lombok.val;

/**
 * @author Dragos
 * @hidden
 */
public class SystemCamera extends SystemBase {
    int resizeWidth, resizeHeight;
    private boolean resizeGlobal = false, resize = false;

    public SystemCamera(Gem ng, SystemConfiguration conf) {
        super(ng, conf);
    }

    @Override
    public void onBeforeUpdate() {
        if (resizeGlobal) {
            resize = true;
        }
    }

    @Override
    public void onUpdate(Entity ent) {
        val camera = ent.getComponent(ComponentCamera.class);
        val viewport = camera.getViewport();
        if (resize) {
        	viewport.setScreenSize(resizeWidth, resizeHeight);
            viewport.apply();
        }
    }

    @Override
    public void onAfterUpdate() {
        resize = false;
    }

    public void resize(int w, int h) {
        resizeGlobal = true;
        resizeWidth = w;
        resizeHeight = h;
    }
}