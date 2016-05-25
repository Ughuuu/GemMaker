package com.ngeen.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.ngeen.component.*;
import com.ngeen.component.ui.ComponentUIStage;
import com.ngeen.entity.Entity;
import com.ngeen.entity.EntityFactory;
import com.ngeen.entity.XmlEntity;
import com.ngeen.systems.*;

import java.util.Set;

/**
 * @author Dragos
 * @composed 1 - 1 SystemBase
 */
public class SystemFactory {
    protected final ComponentFactory _ComponentBuilder;
    protected final Ngeen _Ng;
    private final XmlEntity xmlFactory;
    protected SystemDraw _DrawingSystem;
    protected SystemOverlay _OverlaySystem;
    protected SystemPhysics _PhysicsSystem;
    protected SystemScene _SceneSystem;
    protected SystemSprite _SpriteSystem;
    protected SystemStage _StageSystem;
    private SystemConfiguration _ConfigurationStage;
    private SystemConfiguration _DrawingConfiguration;
    private SystemConfiguration _PointConfiguration;
    private SystemConfiguration _RigidConfiguration;
    private SystemConfiguration _ScriptConfiguration;
    private SystemConfiguration _SpriteConfiguration;

    public SystemFactory(Ngeen ng, ComponentFactory _ComponentBuilder, XmlEntity xml) {
        _Ng = ng;
        xmlFactory = xml;
        this._ComponentBuilder = _ComponentBuilder;
    }

    private void updateSystem(SystemBase system) {
        float time = TimeUtils.millis();
        system.onBeforeUpdate();
        Set<Entity> entities = _Ng.EntityBuilder.getEntitiesForSystem(system);

        for (Entity entity : entities) {
            system.onUpdate(entity);
        }

        system.onAfterUpdate();
        system.deltaTime = TimeUtils.millis() - time;
    }

    protected void createConfigurations() {
        _PointConfiguration = new SystemConfiguration().all(ComponentPoint.class);
        _RigidConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentRigid.class);
        _ScriptConfiguration = new SystemConfiguration().all(ComponentScript.class);
        _DrawingConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentMesh.class,
                ComponentMaterial.class);
        _SpriteConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentSprite.class);
        _ConfigurationStage = new SystemConfiguration().all(ComponentPoint.class, ComponentUIStage.class);
    }

    protected void createMainSystems(SpriteBatch batch) {
        createConfigurations();

        _PhysicsSystem = new SystemPhysics(_Ng, _RigidConfiguration);

        if (EngineInfo.Debug == true) {
            _OverlaySystem = new SystemOverlay(_Ng, _PointConfiguration, batch, xmlFactory);
        }
        _SceneSystem = new SystemScene(_Ng, _ScriptConfiguration, xmlFactory);
        _DrawingSystem = new SystemDraw(_Ng, _DrawingConfiguration);
        _SpriteSystem = new SystemSprite(_Ng, _SpriteConfiguration, batch);
    }

    protected void createUISystems() {
        _StageSystem = new SystemStage(_Ng, _ConfigurationStage, _ComponentBuilder);
    }

    protected void sendConfigurations(EntityFactory EntityBuilder) {
        EntityBuilder.addSystem(_SceneSystem);
        if (EngineInfo.Debug == true) {
            EntityBuilder.addSystem(_OverlaySystem);
        }
        EntityBuilder.addSystem(_PhysicsSystem);
        EntityBuilder.addSystem(_DrawingSystem);
        EntityBuilder.addSystem(_SpriteSystem);
        EntityBuilder.addSystem(_StageSystem);
    }

    protected void updateSystems() {

        updateSystem(_PhysicsSystem);
        updateSystem(_DrawingSystem);
        updateSystem(_SpriteSystem);

        updateSystem(_StageSystem);

        if (EngineInfo.Debug == true) {
            updateSystem(_OverlaySystem);
        }

        updateSystem(_SceneSystem);
    }
}
