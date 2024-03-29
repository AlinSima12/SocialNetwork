package com.example.socialnetwork.domain;

import java.io.Serializable;

public class CommunicationObject implements Serializable {
    public CommunicationObject(Command command, Object object) {
        this.command = command;
        this.object = object;
    }
    Command command;
    Object object;
    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }


}
