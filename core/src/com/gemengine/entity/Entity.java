package com.gemengine.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Entity {
	@Getter private final int id;
	@Getter private final String name;
}
