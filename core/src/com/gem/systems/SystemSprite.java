package com.gem.systems;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.gem.component.ComponentCamera;
import com.gem.component.ComponentSprite;
import com.gem.debug.Debugger;
import com.gem.engine.Gem;
import com.gem.entity.Entity;

/**
 * @author Dragos
 * @hidden
 */
public class SystemSprite extends SystemBase {
    protected SpriteBatch spriteBatch;
    private Matrix4 cameraView;
    private int cameraId;

    public SystemSprite(Gem ng, SystemConfiguration conf, SpriteBatch batch) {
        super(ng, conf);
        spriteBatch = batch;
    }

    @Override
    public void onAfterUpdate() {
        spriteBatch.end();
    }

    @Override
    public void onBeforeUpdate() {
        spriteBatch.begin();
        //spriteBatch.disableBlending();
        spriteBatch.setTransformMatrix(new Matrix4());
    }

    @Override
    public void onUpdate(Entity ent) {
        ComponentCamera camera = Gem.goUpForComponent(ent, ComponentCamera.class);
        if (camera == null) {
            return;
        }
        cameraView = camera.getCombined();
        spriteBatch.setProjectionMatrix(cameraView);

        ComponentSprite sprComp = ent.getComponent(ComponentSprite.class);
        Sprite spr = sprComp.getSprite();

        if (spr.getTexture() == null) {
            Debugger.log(ent.getName() + " doesn't have a right texture.");
            return;
        }

        spr.draw(spriteBatch);
    }
}