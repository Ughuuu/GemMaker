package gemengine.system;

import java.util.HashMap;
import java.util.Map;

import gemengine.system.base.SystemBase;

public class TimingSystem extends SystemBase {
	private Map<String, Float> readings = new HashMap<String, Float>();

	public void addTiming(String marker, float time) {
		readings.put(marker, time);
	}

	public Map<String, Float> getTimings() {
		return readings;
	}
}
