package com.ngeen.systems;

import com.ngeen.component.ComponentDrawble;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public abstract class SystemBase {
	protected boolean Enable;
	protected SystemConfiguration _Config = null;
	public final Ngeen _Ng;
	public float deltaTime;

	public SystemBase(Ngeen ng) {
		_Config = new SystemConfiguration();
		this._Ng = ng;
	}

	public SystemBase(Ngeen ng, SystemConfiguration conf) {
		Enable = true;
		this._Config = conf;
		this._Ng = ng;
	}
	
	public SystemConfiguration getConfiguration(){
		return _Config;
	}

	/**
	 * Enable or Disable this system.
	 * 
	 * @param Enable
	 *            boolean holding the next state of the system.
	 */
	public void setEnable(boolean Enable) {
		this.Enable = Enable;
	}

	/**
	 * Get the state of enable of the system.
	 * 
	 * @return
	 */
	public boolean isEnable() {
		return Enable;
	}

	/**
	 * Called before update, once per frame.
	 */
	public void onBeforeUpdate() {
	}

	/**
	 * Called n times per frame, where n is the number of objects it receives.
	 */
	public void onUpdate(Entity ent) {
	}

	/**
	 * Called after update, once per frame.
	 */
	public void onAfterUpdate() {
	}

	/**
	 * Called when this system is destroyed.
	 */
	public void onDestroy() {
	}
}
