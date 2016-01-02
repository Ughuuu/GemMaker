package com.ngeen.systems;

import com.ngeen.component.DrawbleComponent;
import com.ngeen.entity.Entity;

public abstract class SystemBase {
	protected boolean Enable;
	protected SystemConfiguration config = null;
	
	public SystemBase(SystemConfiguration conf){		
		Enable = true;
		this.config = conf;
	}
	
	/**
	 * Enable or Disable this system.
	 * @param Enable boolean holding the next state of the system.
	 */
	public void setEnable(boolean Enable){
		this.Enable = Enable;
	}
	
	/**
	 * Get the state of enable of the system.
	 * @return
	 */
	public boolean isEnable(){
		return Enable;
	}
	
	/**
	 * Called only once, when this system starts.
	 */
	public abstract void onInit();
	
	/**
	 * Called before update, once per frame.
	 */
	public abstract void onBeforeUpdate();
	
	/**
	 * Called n times per frame, where n is the number of objects it receives.
	 */
	public abstract void onUpdate(Entity ent);
	
	/**
	 * Called after update, once per frame.
	 */
	public abstract void onAfterUpdate();
	
	/**
	 * Called when this system is destroyed.
	 */
	public abstract void onDestroy();
}
