package com.gemengine.system.base;

import com.gemengine.entity.Entity;

public interface EntityListener {
	public static enum EntityChangeType {
		ADD, DELETE, PARENTED, DEPARENTED
	}

	public void onChange(EntityChangeType changeType, Entity first, Entity second);
}
