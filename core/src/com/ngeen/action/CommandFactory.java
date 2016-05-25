package com.ngeen.action;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;

import java.util.Deque;
import java.util.LinkedList;

public class CommandFactory {
    public static CommandFactory factory = new CommandFactory();
    public Ngeen _Ng;
    private Deque<Command> actions = new LinkedList<Command>();
    private Deque<Command> futureActions = new LinkedList<Command>();

    private boolean inProgress = false;

    private Deque<Command> currentCommands= new LinkedList<Command>();
    private int numberOfActions = 0, finalNumberOfActions = 0;

    public CommandFactory() {
    }

    public void doAction(Command action) {
        if (inProgress) {
            return;
        }
        futureActions.clear();
        numberOfActions++;
        currentCommands.addFirst(action);
        if (actions.size() > EngineInfo.CommandsSize) {
            actions.pollLast();
        }
    }

    public void endAction() {
        if (inProgress) {
            return;
        }
        finalNumberOfActions++;
        numberOfActions--;
        if (numberOfActions == 0) {
            currentCommands.getLast().setNumberOfActions(finalNumberOfActions);
            actions.addAll(currentCommands);
            currentCommands.clear();
            finalNumberOfActions = 0;
        }
    }

    public void redoAction() {
        Command command = futureActions.pollLast();
        if (command == null) {
            return;
        }
        inProgress = true;
        Debugger.log(command.getDescription());
        int nrActionsCur = command.getNumberOfActions();
        for (int i = 1; i <= nrActionsCur; i++) {
            if (command != null) {
                actions.add(command);
                command.doCommand();
            }
            if (i == nrActionsCur) {
                break;
            }
            command = futureActions.pollLast();
        }
        inProgress = false;
    }

    public void setNg(Ngeen _Ng) {

    }

    public void undoAction() {
        Command command = actions.pollLast();
        if (command == null) {
            return;
        }
        inProgress = true;
        int nrActionsCur = command.getNumberOfActions();
        for (int i = 1; i <= nrActionsCur; i++) {
            Debugger.log(command.getDescription());
            if (command != null) {
                command.undoCommand();
                futureActions.addLast(command);
            }
            if (i == nrActionsCur) {
                break;
            }
            command = actions.pollLast();
        }
        command.setNumberOfActions(nrActionsCur);
        inProgress = false;
    }
}
