package com.ngeen.tester;

import com.artemis.Entity;
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
import com.ngeen.holder.Ngeen;

public class Test {
	final Ngeen ng;

	public Test(Ngeen ng) {
		this.ng = ng;
		onceTest();
	}

	public void onceTest() {
		Entity ent = ng.entityHelper.createRelational("Test", "~");
	}
}
