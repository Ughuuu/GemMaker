package com.gem.engine;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.gem.component.ComponentCamera;
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
import com.gem.systems.SystemCamera;
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
	protected final ComponentFactory componentBuilder;
	protected final Gem gem;
	private final XmlEntity xmlFactory;
	protected SystemDraw drawingSystem;
	protected SystemOverlay overlaySystem;
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

	protected void createMainSystems(SpriteBatch batch) {
		phisicsSystem = new SystemPhysics(gem, new SystemConfiguration().all(ComponentPoint.class, ComponentRigid.class));

		if (EngineInfo.Debug == true) {
			overlaySystem = new SystemOverlay(gem, new SystemConfiguration().all(ComponentPoint.class), batch, xmlFactory);
		}
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
		if (EngineInfo.Debug == true) {
			EntityBuilder.addSystem(overlaySystem);
		}
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

		if (EngineInfo.Debug == true) {
			updateSystem(overlaySystem);
		}

		updateSystem(sceneSystem);
	}

	public void resize(int w, int h) {
		if(cameraSystem != null){
			cameraSystem.resize(w, h);
		}
	}

	public void restart() {
		if(cameraSystem != null){
			cameraSystem.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}
}
