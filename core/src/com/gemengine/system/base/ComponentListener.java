package com.gemengine.system.base;

import com.gemengine.component.Component;

public interface ComponentListener {
	public static enum ComponentChangeType {
		ADD, DELETE
	}

	public <T extends Component> void onChange(ComponentChangeType changeType, T component);

	public <T extends Component> void onTypeChange(Class<T> type);
}
