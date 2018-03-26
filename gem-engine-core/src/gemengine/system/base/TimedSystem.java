package gemengine.system.base;

import lombok.Getter;

/**
 * A system that has an {@link #onUpdate} event, which is called every
 * {@link #interval} ms.
 * 
 * @author Dragos
 *
 */
public abstract class TimedSystem extends SystemBase {
	/**
	 * The repeat interval in milliseconds. Default is 16, aka 60 fps.
	 */
	@Getter
	private final float interval;

	/**
	 * Create a new timed system with an update interval of 16 ms and priority
	 * max(will be called last in the update).
	 */
	public TimedSystem() {
		this(16, true, Integer.MAX_VALUE);
	}

	/**
	 * Create a new timed system with a given interval to call the
	 * {@link #onUpdate}.
	 * 
	 * @param interval
	 *            The interval
	 * @param enable
	 *            True if this system should work, false otherwise.
	 * @param priority
	 *            Lower means the update is called before other systems that
	 *            have this number higher
	 */
	public TimedSystem(float interval, boolean enable, int priority) {
		super(enable, priority);
		this.interval = interval;
	}

	/**
	 * Called every interval ms.
	 * 
	 * @param delta
	 *            time elapsed from last call.
	 */
	public void onUpdate(float delta) {
	}
}
