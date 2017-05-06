package com.gemengine.listener;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;

public interface AssetListener {
	public void onChange(ChangeType changeType, String oldName, String newName);
}
