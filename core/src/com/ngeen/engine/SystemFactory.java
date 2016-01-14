package com.ngeen.engine;

import java.util.Set;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentMaterial;
import com.ngeen.component.ComponentMesh;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.ComponentRigid;
import com.ngeen.component.ComponentScript;
import com.ngeen.component.ComponentSprite;
import com.ngeen.component.ComponentUILayout;
import com.ngeen.component.ComponentUIStage;
import com.ngeen.component.ComponentUIWidget;
import com.ngeen.debug.Debugger;
import com.ngeen.entity.Entity;
import com.ngeen.entity.EntityFactory;
import com.ngeen.systems.SystemBase;
import com.ngeen.systems.SystemCamera;
import com.ngeen.systems.SystemConfiguration;
import com.ngeen.systems.SystemDraw;
import com.ngeen.systems.SystemOverlay;
import com.ngeen.systems.SystemPhysics;
import com.ngeen.systems.SystemScene;
import com.ngeen.systems.SystemSprite;
import com.ngeen.systems.SystemStage;
import com.ngeen.systems.SystemTable;
import com.ngeen.systems.SystemWidget;

public class SystemFactory {
	protected final Ngeen _Ng;

	protected SystemCamera _SystemCamera;
	protected SystemScene _SceneSystem;
	protected SystemOverlay _OverlaySystem;
	protected SystemPhysics _PhysicsSystem;
	protected SystemDraw _DrawingSystem;
	protected SystemSprite _SpriteSystem;
	protected SystemStage _StageSystem;
	protected SystemTable _TableSystem;
	protected SystemWidget _WidgetSystem;
	
	private SystemConfiguration _PointConfiguration;
	private SystemConfiguration _RigidConfiguration;
	private SystemConfiguration _ScriptConfiguration;
	private SystemConfiguration _DrawingConfiguration;
	private SystemConfiguration _SpriteConfiguration;
	private SystemConfiguration _CameraConfiguration;
	private SystemConfiguration _ConfigurationTable;
	private SystemConfiguration _ConfigurationWidget;
	private SystemConfiguration _ConfigurationStage;
	
	public SystemFactory(Ngeen ng){
		_Ng = ng;
	}

	protected void createConfigurations() {
		_PointConfiguration = new SystemConfiguration().all(ComponentPoint.class);
		_RigidConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentRigid.class);
		_ScriptConfiguration = new SystemConfiguration().all(ComponentScript.class);
		_DrawingConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentMesh.class,
				ComponentMaterial.class);
		_SpriteConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentSprite.class);
		_CameraConfiguration = new SystemConfiguration().all(ComponentPoint.class, ComponentCamera.class);
		_ConfigurationTable = new SystemConfiguration().all(ComponentPoint.class, ComponentUILayout.class);
		_ConfigurationWidget = new SystemConfiguration().all(ComponentPoint.class, ComponentUIWidget.class);
		_ConfigurationStage = new SystemConfiguration().all(ComponentPoint.class, ComponentUIStage.class);
	}
	
	protected void sendConfigurations(EntityFactory EntityBuilder){
		EntityBuilder.addSystem(_SceneSystem);
		EntityBuilder.addSystem(_OverlaySystem);
		EntityBuilder.addSystem(_PhysicsSystem);
		EntityBuilder.addSystem(_DrawingSystem);
		EntityBuilder.addSystem(_SpriteSystem);
		EntityBuilder.addSystem(_SystemCamera);
		EntityBuilder.addSystem(_TableSystem);
		EntityBuilder.addSystem(_WidgetSystem);
		EntityBuilder.addSystem(_StageSystem);
	}
	
	protected void createMainSystems(SpriteBatch batch){
		createConfigurations();
		
		_PhysicsSystem = new SystemPhysics(_Ng, _RigidConfiguration);
		_OverlaySystem = new SystemOverlay(_Ng, _PointConfiguration, batch);
		_SceneSystem = new SystemScene(_Ng, _ScriptConfiguration);
		_DrawingSystem = new SystemDraw(_Ng, _DrawingConfiguration);
		_SpriteSystem = new SystemSprite(_Ng, _SpriteConfiguration, batch);
		_SystemCamera = new SystemCamera(_Ng, _CameraConfiguration);
	}


	protected void createUISystems() {
		_TableSystem = new SystemTable(_Ng, _ConfigurationTable);
		_WidgetSystem = new SystemWidget(_Ng, _ConfigurationWidget);
		_StageSystem = new SystemStage(_Ng, _ConfigurationStage);
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
	
	protected void updateSystems(){
		updateSystem(_SceneSystem);
		updateSystem(_PhysicsSystem);
		updateSystem(_DrawingSystem);
		updateSystem(_SpriteSystem);
		updateSystem(_SystemCamera);

		updateSystem(_TableSystem);
		updateSystem(_WidgetSystem);
		updateSystem(_StageSystem);
		
		updateSystem(_OverlaySystem);
	}
}
