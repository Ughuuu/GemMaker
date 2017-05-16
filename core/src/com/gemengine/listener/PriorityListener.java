package com.gemengine.listener;

import java.util.Comparator;

public interface PriorityListener {
	public int getPriority();

	public boolean isEnable();

	public static Comparator<PriorityListener> getComparator() {
		return new Comparator<PriorityListener>() {

			@Override
			public int compare(PriorityListener o1, PriorityListener o2) {
				if (o1.getPriority() < o2.getPriority()) {
					return -1;
				}
				if (o1.getPriority() > o2.getPriority()) {
					return 1;
				}
				return 0;
			}
		};
	}
}
