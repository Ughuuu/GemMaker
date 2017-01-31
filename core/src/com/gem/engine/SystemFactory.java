package com.gem.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.gem.component.*;
import com.gem.component.ui.ComponentUIStage;
import com.gem.entity.Entity;
import com.gem.entity.EntityFactory;
import com.gem.entity.XmlEntity;
import com.gem.systems.*;

import java.util.Set;

/**
 * @author Dragos
 * @composed 1 - 1 SystemBase
 */
public class SystemFactory {
    protected final ComponentFactory componentBuilder;
    protected final Gem gem;
    private final XmlEntity xmlFactory;
    protected SystemDraw drawingSystem;
    protected SystemPhysics phisicsSystem;
    protected SystemScene sceneSystem;
    protected SystemSprite spriteSystem;
    protected SystemCamera cameraSystem;
    protected SystemStage stageSystem;

    public SystemFactory(Gem ng, ComponentFactory _ComponentBuilder, XmlEntity xml) {
        gem = ng;
        xmlFactory = xml;
        this.componentBuilder = _ComponentBuilder;
    }
    
    private void updateSystem(SystemBase system) {
        float time = TimeUtils.millis();
        system.onBeforeUpdate();
        Set<Entity> entities = gem.entityBuilder.getEntitiesForSystem(system);

        for (Entity entity : entities) {
            system.onUpdate(entity);
        }

        system.onAfterUpdate();
        system.deltaTime = TimeUtils.millis() - time;
    }
    
    private void updateAloneSystem(SystemBase system) {
        float time = TimeUtils.millis();
        system.onBeforeUpdate();
        Set<Entity> entities = gem.entityBuilder.getEntitiesForSystem(system);

        system.onUpdate(entities);

        system.onAfterUpdate();
        system.deltaTime = TimeUtils.millis() - time;
    }

    protected void createMainSystems(SpriteBatch batch) {
        phisicsSystem = new SystemPhysics(gem, new SystemConfiguration().all(ComponentPoint.class, ComponentRigid.class));

        cameraSystem = new SystemCamera(gem, new SystemConfiguration().all(ComponentPoint.class, ComponentCamera.class));
        sceneSystem = new SystemScene(gem, new SystemConfiguration().all(ComponentScript.class), xmlFactory);
        drawingSystem = new SystemDraw(gem, new SystemConfiguration().all(ComponentPoint.class, ComponentMesh.class,
                ComponentMaterial.class));
        spriteSystem = new SystemSprite(gem, new SystemConfiguration().all(ComponentPoint.class, ComponentSprite.class), batch);
    }

    protected void createUISystems() {
        stageSystem = new SystemStage(gem, new SystemConfiguration().all(ComponentPoint.class, ComponentUIStage.class), componentBuilder);
    }

    protected void sendConfigurations(EntityFactory EntityBuilder) {
        EntityBuilder.addSystem(sceneSystem);
        EntityBuilder.addSystem(phisicsSystem);
        EntityBuilder.addSystem(drawingSystem);
        EntityBuilder.addSystem(spriteSystem);
        EntityBuilder.addSystem(stageSystem);
    }

    protected void updateSystems() {
        updateSystem(phisicsSystem);
        updateSystem(drawingSystem);
        updateSystem(spriteSystem);

        updateSystem(stageSystem);

        updateAloneSystem(sceneSystem);
    }

    public void resize(int w, int h) {
        if (cameraSystem != null) {
            cameraSystem.resize(w, h);
        }
    }

    public void restart() {
        if (cameraSystem != null) {
            cameraSystem.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }
}
