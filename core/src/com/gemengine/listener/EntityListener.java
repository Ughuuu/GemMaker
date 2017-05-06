package com.gemengine.listener;

import com.gemengine.entity.Entity;

public interface EntityListener {
	public static enum EntityChangeType {
		ADD, DELETE, PARENTED, DEPARENTED
	}

	public void onChange(EntityChangeType changeType, Entity first, Entity second);
}
