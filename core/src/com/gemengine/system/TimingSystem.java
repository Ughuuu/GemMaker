package com.gemengine.system;

import java.util.HashMap;
import java.util.Map;

import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;

public class TimingSystem extends SystemBase {
	private Map<String, Float> readings = new HashMap<String, Float>();

	public void addTiming(String marker, float time, float interval) {
		readings.put(marker, time / interval);
	}

	public Map<String, Float> getTimings() {
		return readings;
	}
}
