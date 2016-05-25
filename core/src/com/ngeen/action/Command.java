package com.ngeen.action;

import com.ngeen.debug.Debugger;

public class Command {
    private final Function functionDo, functionRedo;
    private int numberOfActions = 1;
    private String description;

    public Command(Function lambdaDo, Function lambdaRedo, String desc) {
        functionDo = lambdaDo;
        functionRedo = lambdaRedo;
        description = desc;
    }

    public void doCommand() {
        functionDo.doAction();
    }

    public String getDescription(){
        return description;
    }

    public int getNumberOfActions() {
        return numberOfActions;
    }

    public void setNumberOfActions(int actions) {
        numberOfActions = actions;
    }

    public void undoCommand() {
        functionRedo.doAction();
    }
}
