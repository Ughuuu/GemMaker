package com.gemengine.system.base;

import com.gemengine.component.Component;
import com.gemengine.entity.Entity;

public interface EntityComponentListener {
	public Entity getOwner();

	public <T extends Component> void onNotify(String event, T notifier);
}
