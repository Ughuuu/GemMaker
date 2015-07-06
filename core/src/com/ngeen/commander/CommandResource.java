package com.ngeen.commander;

import com.ngeen.command.Command;
import com.ngeen.components.ResourceComponent;
import com.ngeen.holder.Ngeen;

public class CommandResource<T> implements Command{
	public CommandResource(ResourceComponent<T> com) {

	}

	@Override
	public void redoNext(Ngeen ng) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void undoNext(Ngeen ng) {
		// TODO Auto-generated method stub
		
	}
}
