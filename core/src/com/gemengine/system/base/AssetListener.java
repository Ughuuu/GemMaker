package com.gemengine.system.base;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;

public interface AssetListener {
	public void onChange(ChangeType changeType, String oldName, String newName);
}
