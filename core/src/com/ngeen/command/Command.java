package com.ngeen.command;

import com.artemis.Entity;
import com.ngeen.holder.Ngeen;

public interface Command {
	public void redoNext(Ngeen ng);
	public void undoNext(Ngeen ng);
}
