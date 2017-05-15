package com.gemengine.listener;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;

/**
 * Listener interface used for assets. Extend this and add yourself as a
 * listener in the {@link com.gemengine.system.AssetSystem} by calling
 * {@link com.gemengine.system.AssetSystem#addAssetListener}
 * 
 * @author Dragos
 *
 */
public interface AssetListener extends PriorityListener{
	/**
	 * Triggered when an asset has changed externally or interanlly(add only).
	 * For delete, add, modify events, both names are the same. For copy and
	 * rename event, the old name is the old name of the asset, the newName is
	 * the new name of the asset.
	 * 
	 * @param changeType
	 *            The type of the change.
	 * @param oldName
	 *            The old name of this asset.
	 * @param newName
	 *            The new name of this asset.
	 */
	public void onChange(ChangeType changeType, String oldName, String newName);
}
