package com.gemengine.system.common;

/**
 * The base system for all other systems. Implement this if you want a system
 * with the basic events.
 *
 * @author Dragos
 *
 */
public abstract class SystemBase implements Comparable<SystemBase> {
    private boolean enable;

    /**
     * The systems are called on their events based on their priorities.
     */
    private final int priority;

    /**
     * Create a system with priority as Integer.MAX_VALUE(will be called last).
     */
    public SystemBase() {
        this(true, Integer.MAX_VALUE);
    }

    public SystemBase(boolean enable, int priority) {
        this.enable = enable;
        this.priority = priority;
    }

    @Override
    public int compareTo(SystemBase other) {
        if (this.getPriority() < other.getPriority()) {
            return -1;
        }
        if (this.getPriority() > other.getPriority()) {
            return 1;
        }
        return 0;
    }

    public void onDestroy() {
    }

    public void onCreate() {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getPriority() {
        return priority;
    }
}
