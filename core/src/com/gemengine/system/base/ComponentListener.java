package com.gemengine.system.base;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;

public interface ComponentListener {
	public void onUpdate(Class<?> type);
}
