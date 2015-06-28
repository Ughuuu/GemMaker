package com.ngeen.helper;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ngeen.components.AnimationComponent;
import com.ngeen.components.GroupComponent;
import com.ngeen.components.ResourceComponent;
import com.ngeen.components.TagComponent;
import com.ngeen.components.TextureComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.factories.CollidableFactory;

public class EntityHelper<T> {
	CollidableFactory collidableFactory;
	Archetype transform, empty, resource;
	World engine;

	@SuppressWarnings("unchecked")
	public EntityHelper(World engine) {
		this.engine = engine;
		final ArchetypeBuilder archetypeBuilder = new ArchetypeBuilder();
		transform = archetypeBuilder.add(TransformComponent.class,
				TagComponent.class, GroupComponent.class).build(engine);
		empty = archetypeBuilder.add(TagComponent.class, GroupComponent.class)
				.build(engine);
		resource = archetypeBuilder.add(ResourceComponent.class,
				TagComponent.class, GroupComponent.class).build(engine);
	}

	public Entity createPositional(String tag, String group) {
		if (engine.getManager(TagManager.class).isRegistered(tag))
			return null;
		Entity ent = engine.createEntity(transform);
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

	public Entity createResource(T resource_object, String tag, String group) {
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
}
