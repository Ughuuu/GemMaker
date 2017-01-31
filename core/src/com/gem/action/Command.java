package com.gem.action;

import lombok.Getter;
import lombok.Setter;

public class Command {
    private final Function functionDo, functionRedo;
    @Getter
    @Setter
    private int numberOfActions = 1;
    @Getter
    private String description;

    public Command(Function lambdaDo, Function lambdaRedo, String desc) {
        functionDo = lambdaDo;
        functionRedo = lambdaRedo;
        description = desc;
    }

    public void doCommand() {
        functionDo.doAction();
    }

    public void undoCommand() {
        functionRedo.doAction();
    }
}
