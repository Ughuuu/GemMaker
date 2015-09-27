package com.ngeen.helper;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.ngeen.components.AnimationComponent;
import com.ngeen.components.ButtonComponent;
import com.ngeen.components.CameraComponent;
import com.ngeen.components.GroupComponent;
import com.ngeen.components.MusicComponent;
import com.ngeen.components.ParticleComponent;
import com.ngeen.components.PhysicsComponent;
import com.ngeen.components.ResourceComponent;
import com.ngeen.components.ScriptComponent;
import com.ngeen.components.SoundComponent;
import com.ngeen.components.TagComponent;
import com.ngeen.components.TextComponent;
import com.ngeen.components.TextureComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.factories.CollidableFactory;
import com.ngeen.ui.Interface;

public class EntityHelper {
	CollidableFactory collidableFactory;
	Archetype transform, empty, resource;
	World engine;

	@SuppressWarnings("unchecked")
	public EntityHelper(World engine) {
		this.engine = engine;
		transform = new ArchetypeBuilder().add(TransformComponent.class,
				TagComponent.class, GroupComponent.class).build(engine);
		empty = new ArchetypeBuilder().add(TagComponent.class,
				GroupComponent.class).build(engine);
		resource = new ArchetypeBuilder().add(ResourceComponent.class,
				TagComponent.class, GroupComponent.class).build(engine);
	}

	public Entity createPositional(String tag, String group) {
		if (engine.getManager(TagManager.class).isRegistered(tag))
			return null;
		Entity ent = engine.createEntity(transform);
		ent.getComponent(TransformComponent.class).scale = new Vector2(1,1);
		ent.getComponent(TagComponent.class).name = tag;
		ent.getComponent(GroupComponent.class).name = group;
		engine.getManager(TagManager.class).register(tag, ent);
		engine.getManager(GroupManager.class).add(ent, group);
		return ent;
	}

	public Entity createRelational(String tag, String group) {
		if (engine.getManager(TagManager.class).isRegistered(tag))
			return null;
		Entity ent = engine.createEntity(empty);
		ent.getComponent(TagComponent.class).name = tag;
		ent.getComponent(GroupComponent.class).name = group;
		engine.getManager(TagManager.class).register(tag, ent);
		engine.getManager(GroupManager.class).add(ent, group);
		return ent;
	}

	public Entity createResource(Object resource_object, String tag,
			String group) {
		if (engine.getManager(TagManager.class).isRegistered(tag))
			return null;
		Entity ent = engine.createEntity(resource);
		ent.getComponent(TagComponent.class).name = tag;
		ent.getComponent(GroupComponent.class).name = group;
		engine.getManager(TagManager.class).register(tag, ent);
		engine.getManager(GroupManager.class).add(ent, group);
		ent.getComponent(ResourceComponent.class).resource = resource_object;
		return ent;
	}

	public AnimationComponent addAnimation(Entity e) {
		AnimationComponent anim = e.edit().create(AnimationComponent.class);
		return anim;
	}

	public void removeComponent(Class<? extends Component> component, Entity e) {
		e.edit().remove(component);
	}

	public void addComponent(Class<? extends Component> component, Entity e) {
		Component com = e.edit().create(component);
		if (component.equals(AnimationComponent.class)) {

		}
		if (component.equals(ButtonComponent.class)) {

		}
		if (component.equals(CameraComponent.class)) {

		}
		if (component.equals(MusicComponent.class)) {

		}
		if (component.equals(ParticleComponent.class)) {

		}
		if (component.equals(PhysicsComponent.class)) {

		}
		if (component.equals(ResourceComponent.class)) {

		}
		if (component.equals(ScriptComponent.class)) {

		}
		if (component.equals(SoundComponent.class)) {

		}
		if (component.equals(TextComponent.class)) {

		}
		if (component.equals(TextureComponent.class)) {
			Entity res = Interface.ng.getByTag("n.png");
			TextureComponent texture = (TextureComponent)com;
			texture.resource_index = res.id;
			texture.tex = new Sprite((Texture)res.getComponent(ResourceComponent.class).resource);
		}
		if (component.equals(TransformComponent.class)) {
			TransformComponent transf = (TransformComponent)com;
			transf.scale = new Vector2(1,1);
			transf.position = new Vector2(0,0);
		}
	}
}
