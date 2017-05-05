package com.gemengine.system.base;

import java.util.Set;

import com.gemengine.component.Component;

public interface ComponentListener {
	public static enum ComponentChangeType {
		ADD, DELETE
	}

	public Set<String> getConfiguration();

	public <T extends Component> void onChange(ComponentChangeType changeType, T component);

	public <T extends Component> void onNotify(String event, T notifier);

	public <T extends Component> void onTypeChange(Class<T> type);
}
