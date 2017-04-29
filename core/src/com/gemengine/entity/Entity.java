package com.gemengine.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Entity {
	@Getter
	private final int id;
	@Getter
	private final String name;
	private static int lastId;

	public Entity(String name) {
		this.name = name;
		this.id = lastId++;
	}
}
