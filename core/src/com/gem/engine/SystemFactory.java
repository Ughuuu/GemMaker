package com.gem.engine;

import java.util.Set;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.gem.component.ComponentFactory;
import com.gem.component.ComponentMaterial;
import com.gem.component.ComponentMesh;
import com.gem.component.ComponentPoint;
import com.gem.component.ComponentRigid;
import com.gem.component.ComponentScript;
import com.gem.component.ComponentSprite;
import com.gem.component.ui.ComponentUIStage;
import com.gem.entity.Entity;
import com.gem.entity.EntityFactory;
import com.gem.entity.XmlEntity;
import com.gem.systems.SystemBase;
import com.gem.systems.SystemConfiguration;
import com.gem.systems.SystemDraw;
import com.gem.systems.SystemOverlay;
import com.gem.systems.SystemPhysics;
import com.gem.systems.SystemScene;
import com.gem.systems.SystemSprite;
import com.gem.systems.SystemStage;

/**
 * @author Dragos
 * @composed 1 - 1 SystemBase
 */
public class SystemFactory {
	protected final ComponentFactory _ComponentBuilder;
	protected final Gem _Ng;
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

	public SystemFactory(Gem ng, ComponentFactory _ComponentBuilder, XmlEntity xml) {
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
