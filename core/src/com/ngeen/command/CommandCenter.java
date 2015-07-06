package com.ngeen.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artemis.Entity;
import com.ngeen.commander.CommandAnimation;
import com.ngeen.commander.CommandButton;
import com.ngeen.commander.CommandCamera;
import com.ngeen.commander.CommandDelete;
import com.ngeen.commander.CommandGroup;
import com.ngeen.commander.CommandMusic;
import com.ngeen.commander.CommandParticle;
import com.ngeen.commander.CommandPhysics;
import com.ngeen.commander.CommandResource;
import com.ngeen.commander.CommandScript;
import com.ngeen.commander.CommandSound;
import com.ngeen.commander.CommandTag;
import com.ngeen.commander.CommandText;
import com.ngeen.commander.CommandTexture;
import com.ngeen.commander.CommandTransform;
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

public class CommandCenter {
	private int time = 0, maxTime = 0, minTime = 0;
	private Map<Integer, ArrayList<Integer>> object_to_date, date_to_object;
	private Map<Integer, HashMap<Integer, ArrayList<Command>>> date_to_object_command;
	private final Ngeen ng;

	public CommandCenter(Ngeen ng) {
		this.ng = ng;
		object_to_date = new HashMap<Integer, ArrayList<Integer>>();
		date_to_object = new HashMap<Integer, ArrayList<Integer>>();
		date_to_object_command = new HashMap<Integer, HashMap<Integer, ArrayList<Command>>>();
	}

	private void clearTime(int date) {
		for (int i = 0; i < date_to_object.get(date).size(); i++) {
			object_to_date.get(date_to_object.get(i)).clear();
			object_to_date.remove(date_to_object.get(i));
			date_to_object_command.get(date).clear();
			date_to_object_command.remove(date);
		}
	}

	public void delNext(Entity e) {
		ArrayList<Command> components = null;
		if (object_to_date.get(e.getId()) == null) {
			object_to_date.put(e.getId(), new ArrayList<Integer>());
		}
		if (date_to_object.get(time) == null) {
			date_to_object.put(time, new ArrayList<Integer>());
		}
		if (date_to_object_command.get(time) == null) {
			date_to_object_command.put(time,
					new HashMap<Integer, ArrayList<Command>>());
		}

		if (date_to_object_command.get(time).get(e.getId()) == null) {
			components = new ArrayList<Command>();
			date_to_object_command.get(time).put(e.getId(), components);
		} else {
			components = date_to_object_command.get(time).get(e.getId());
		}

		object_to_date.get(e.getId()).add(time);
		date_to_object.get(time).add(e.getId());

		components.add(new CommandDelete(e.getComponent(TagComponent.class)));
	}

	public void doNext(Entity e) {
		ArrayList<Command> components = null;
		if (object_to_date.get(e.getId()) == null) {
			object_to_date.put(e.getId(), new ArrayList<Integer>());
		}
		if (date_to_object.get(time) == null) {
			date_to_object.put(time, new ArrayList<Integer>());
		}
		if (date_to_object_command.get(time) == null) {
			date_to_object_command.put(time,
					new HashMap<Integer, ArrayList<Command>>());
		}

		if (date_to_object_command.get(time).get(e.getId()) == null) {
			components = new ArrayList<Command>();
			date_to_object_command.get(time).put(e.getId(), components);
		} else {
			components = date_to_object_command.get(time).get(e.getId());
		}

		object_to_date.get(e.getId()).add(time);
		date_to_object.get(time).add(e.getId());

		AnimationComponent anim = e.getComponent(AnimationComponent.class);
		if (anim != null) {
			components.add(new CommandAnimation(anim));
		}
		ButtonComponent butc = e.getComponent(ButtonComponent.class);
		if (butc != null) {
			components.add(new CommandButton(butc));
		}
		CameraComponent camc = e.getComponent(CameraComponent.class);
		if (camc != null) {
			components.add(new CommandCamera(camc));
		}
		GroupComponent groupc = e.getComponent(GroupComponent.class);
		if (groupc != null) {
			components.add(new CommandGroup(groupc));
		}
		MusicComponent musicc = e.getComponent(MusicComponent.class);
		if (musicc != null) {
			components.add(new CommandMusic(musicc));
		}
		ParticleComponent partc = e.getComponent(ParticleComponent.class);
		if (partc != null) {
			components.add(new CommandParticle(partc));
		}
		PhysicsComponent physc = e.getComponent(PhysicsComponent.class);
		if (physc != null) {
			components.add(new CommandPhysics(physc));
		}
		ResourceComponent resc = e.getComponent(ResourceComponent.class);
		if (resc != null) {
			components.add(new CommandResource(resc));
		}
		ScriptComponent scriptc = e.getComponent(ScriptComponent.class);
		if (scriptc != null) {
			components.add(new CommandScript(scriptc));
		}
		SoundComponent soundc = e.getComponent(SoundComponent.class);
		if (soundc != null) {
			components.add(new CommandSound(soundc));
		}
		TagComponent tagc = e.getComponent(TagComponent.class);
		if (tagc != null) {
			components.add(new CommandTag(tagc));
		}
		TextComponent textc = e.getComponent(TextComponent.class);
		if (textc != null) {
			components.add(new CommandText(textc));
		}
		TextureComponent texturec = e.getComponent(TextureComponent.class);
		if (texturec != null) {
			components.add(new CommandTexture(texturec));
		}
		TransformComponent transformc = e
				.getComponent(TransformComponent.class);
		if (transformc != null) {
			components.add(new CommandTransform(transformc));
		}
	}

	public boolean increaseTime() {
		boolean ok = false;
		if (maxTime == time) {
			maxTime++;
			ok = true;
		} else {
			clearTime(time + 1);
		}
		time++;
		if (maxTime - minTime > 20) {
			clearTime(minTime);
			minTime++;
		}
		return ok;
	}

	public void decreaseTime() {
		time--;
	}

	public void undoNext() {
		ArrayList<Integer> objects = date_to_object.get(time);
		if (objects != null) {
			HashMap<Integer, ArrayList<Command>> mapped_time = date_to_object_command
					.get(time);
			for (int i = 0; i < objects.size(); i++) {
				ArrayList<Command> commands = mapped_time.get(objects.get(i));
				if (commands != null) {
					for (int j = 0; j < commands.size(); j++) {
						commands.get(i).undoNext(ng);
					}
				}
			}
		}
	}

	public void redoNext() {
		ArrayList<Integer> objects = date_to_object.get(time);
		if (objects != null) {
			HashMap<Integer, ArrayList<Command>> mapped_time = date_to_object_command
					.get(time);
			for (int i = 0; i < objects.size(); i++) {
				ArrayList<Command> commands = mapped_time.get(objects.get(i));
				if (commands != null) {
					for (int j = 0; j < commands.size(); j++) {
						commands.get(i).redoNext(ng);
					}
				}
			}
		}
	}
}
